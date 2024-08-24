package org.test.mastodon.postgateway.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FanoutWebSocketBroker implements WebSocketBroker {

	private static final Logger LOG = LoggerFactory.getLogger(FanoutWebSocketBroker.class);
	private Map<String, WebSocketSession> sessions = new HashMap<>();

	public void registerSession(WebSocketSession s) {
		sessions.put(s.getId(), s);
	}

	public void removeSession(String id) {
		sessions.remove(id);
	}

	public void deliverPost(MastodonPost message) {
		sessions.forEach((id, wss) -> {
			try {
				if (wss.isOpen()) {
					wss.sendMessage(new TextMessage(message.getContent()));
					LOG.trace("Post id: {}, delivered to WS Session: {}", message.getId(), wss.getId());
				}
			} catch (IOException e) {
				LOG.error("Exception while delivering message to WebSocket, skipping it", e);
			}
		});
	}

}