package org.test.mastodon.mconnector.infra;

import org.mastodon4j.core.MastodonClient;
import org.mastodon4j.core.api.MastodonApi;
import org.mastodon4j.core.api.entities.AccessToken;
import org.mastodon4j.core.api.entities.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.test.mastodon.mconnector.model.service.MastodonEventProcessor;

@Service
public class MastodonConfig {

	@Autowired
	private MastodonEventProcessor eventProcessor;
	private static final Logger LOG = LoggerFactory.getLogger(MastodonConfig.class);
	@Value("${mastodon.ulr}")
	private String instanceUrl;
	@Value("${mastodon.access_token}")
	private String accessToken;
	@Bean
	public MastodonApi mastodonApi() {
		var token = AccessToken.create(accessToken);
		MastodonApi client = MastodonClient.create(instanceUrl, token);
		var stream = client.streaming().stream();

		stream.registerConsumer(e -> {
			try {
				eventProcessor.process(e);
			} catch (Throwable throwable) {
				LOG.error("Exception while handling post to a stream", throwable);
			}
		});

		Subscription subscription = Subscription.stream(true, token, "public");
		stream.changeSubscription(subscription);

		return client;
	}


}
