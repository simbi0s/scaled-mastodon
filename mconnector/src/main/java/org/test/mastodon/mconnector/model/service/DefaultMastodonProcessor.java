package org.test.mastodon.mconnector.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mastodon4j.core.api.entities.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.mastodon.mconnector.model.MastodonPost;

@Service
public class DefaultMastodonProcessor implements MastodonEventProcessor{

	private RabbitOperations rabbitOperations;

	public DefaultMastodonProcessor(@Autowired RabbitOperations rabbitOperations) {
		this.rabbitOperations = rabbitOperations;
	}

	private ObjectMapper mapper = new ObjectMapper();

	private static final Logger LOG = LoggerFactory.getLogger(DefaultMastodonProcessor.class);
	@Override
	public void process(Event event) {
		if (event.event().equals("update")) {
			LOG.debug("Received event: {}...", String.valueOf(event.payload().toCharArray(), 0, event.payload().length() > 70 ? 70 : event.payload().length()) );
			try {
				var post = mapper.readValue(event.payload(), MastodonPost.class);
				String value = mapper.writeValueAsString(post);
				rabbitOperations.convertAndSend("posts-stream-n1",value);
			} catch (Exception ex) {
				LOG.error("Exception while publishing post to a stream, skipping it", ex);
			}
		}
	}
}
