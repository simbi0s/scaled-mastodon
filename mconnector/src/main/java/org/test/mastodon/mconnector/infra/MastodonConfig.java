package org.test.mastodon.mconnector.infra;

import jakarta.annotation.PostConstruct;
import org.mastodon4j.core.MastodonClient;
import org.mastodon4j.core.api.EventStream;
import org.mastodon4j.core.api.MastodonApi;
import org.mastodon4j.core.api.entities.AccessToken;
import org.mastodon4j.core.api.entities.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.mastodon.mconnector.model.service.MastodonEventProcessor;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class MastodonConfig {

	@Autowired
	private MastodonEventProcessor eventProcessor;
	private static final Logger LOG = LoggerFactory.getLogger(MastodonConfig.class);
	@Value("${mastodon.ulr}")
	private String instanceUrl;
	@Value("${mastodon.access_token}")
	private String accessToken;
	private volatile MastodonApi mastodonApi;
	private volatile EventStream stream;

	@PostConstruct
	public void mastodonApiInit() {
		var token = AccessToken.create(accessToken);
		connectToMastodonApi(token);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					if (mastodonApi != null && stream != null) {
						Subscription subscription = Subscription.stream(true, token, "public");
						stream.changeSubscription(subscription);
					} else {
						connectToMastodonApi(token);
					}
				} catch (Exception exception) {
					LOG.error("Exception while resubscribing WebSocket on Mastodon API, reestablish connectivity", exception);
					connectToMastodonApi(token);
				}
			}

		}, 0, 2000);

	}

	private void connectToMastodonApi(AccessToken accessToken) {
		try {
			mastodonApi = MastodonClient.create(instanceUrl, accessToken);
			stream = mastodonApi.streaming().stream();

			stream.registerConsumer(e -> {
				try {
					eventProcessor.process(e);
				} catch (Throwable throwable) {
					LOG.error("Exception while handling post to a stream", throwable);
				}
			});
			Subscription subscription = Subscription.stream(true, accessToken, "public");
			stream.changeSubscription(subscription);
		} catch (Exception ex2) {
			LOG.error("Exception while reconnecting WebSocket to Mastodon API", ex2);
		}
	}

}
