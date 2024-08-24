package org.test.mastodon.postgateway.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.test.mastodon.postgateway.model.WebSocketBroker;

public class PostsWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketBroker registry;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        registry.registerSession(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        registry.removeSession(session.getId());
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }

}