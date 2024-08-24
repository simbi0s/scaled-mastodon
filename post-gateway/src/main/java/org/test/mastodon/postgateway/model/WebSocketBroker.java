package org.test.mastodon.postgateway.model;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketBroker {

	void registerSession(WebSocketSession s);

	void removeSession(String id);

	void deliverPost(MastodonPost message);

}
