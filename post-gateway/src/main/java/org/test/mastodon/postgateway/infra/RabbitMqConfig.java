package org.test.mastodon.postgateway.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.mastodon.postgateway.model.FanoutWebSocketBroker;
import org.test.mastodon.postgateway.model.MastodonPost;

@Configuration
public class RabbitMqConfig {

	@Autowired
	private FanoutWebSocketBroker hub;

	@Autowired
	private ObjectMapper objectMapper;

	private Logger LOG = LoggerFactory.getLogger(RabbitMqConfig.class);

	@Bean
	Queue stream() {
		return QueueBuilder.durable("posts-stream-n1")
				.ttl(60 * 1000)
				.stream()
				.build();
	}

	@RabbitListener(queues = "posts-stream-n1")
	void listen(String in) {
		try {
			var post = objectMapper.readValue(in, MastodonPost.class);
			LOG.debug("Received post, id: {}", post.getId());
			hub.deliverPost(post);
		} catch (Exception e) {
			LOG.error("Exception while unmarshalling message", e);
		}
	}
}
