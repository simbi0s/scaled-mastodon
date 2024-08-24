package org.test.mastodon.mconnector.model.service;

import org.mastodon4j.core.api.entities.Event;

public interface MastodonEventProcessor {

	void process(Event event);

}
