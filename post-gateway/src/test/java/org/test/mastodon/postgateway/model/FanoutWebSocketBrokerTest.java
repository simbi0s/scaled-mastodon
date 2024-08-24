package org.test.mastodon.postgateway.model;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FanoutWebSocketBrokerTest {

	@Test
	void registerSession() {
		FanoutWebSocketBroker broker = new FanoutWebSocketBroker();
		WebSocketSession session = Mockito.mock(WebSocketSession.class);
		Mockito.when(session.getId()).thenReturn("WSS-ID-1");
		broker.registerSession(session);
	}

	@Test
	void removeSession() {
		FanoutWebSocketBroker broker = new FanoutWebSocketBroker();
		WebSocketSession session = Mockito.mock(WebSocketSession.class);
		Mockito.when(session.getId()).thenReturn("WSS-ID-1");
		broker.registerSession(session);

		broker.removeSession(session.getId());
	}

	@Test
	void twoWsSession_deliverPost_allSessionsReceivedPost() throws IOException {
		FanoutWebSocketBroker broker = new FanoutWebSocketBroker();
		WebSocketSession session1 = Mockito.mock(WebSocketSession.class);
		Mockito.when(session1.getId()).thenReturn("WSS-ID-1");
		Mockito.when(session1.isOpen()).thenReturn(true);
		broker.registerSession(session1);

		WebSocketSession session2 = Mockito.mock(WebSocketSession.class);
		Mockito.when(session2.getId()).thenReturn("WSS-ID-2");
		Mockito.when(session2.isOpen()).thenReturn(true);
		broker.registerSession(session2);

		MastodonPost post = new MastodonPost();
		MastodonAccount account = new MastodonAccount();
		account.setUsername("USER-1");
		post.setAccount(account);
		post.setCreatedAt("12/01/2024");
		post.setContent("JSON VALUE");

		broker.deliverPost(post);

		ArgumentCaptor<TextMessage> textMessageCaptor1 = ArgumentCaptor.forClass(TextMessage.class);
		Mockito.verify(session1, Mockito.times(1)).sendMessage(textMessageCaptor1.capture());

		ArgumentCaptor<TextMessage> textMessageCaptor2 = ArgumentCaptor.forClass(TextMessage.class);
		Mockito.verify(session2, Mockito.times(1)).sendMessage(textMessageCaptor2.capture());

		TextMessage textMessageValue1 = textMessageCaptor1.getValue();
		assertEquals("JSON VALUE", textMessageValue1.getPayload());

		TextMessage textMessageValue2 = textMessageCaptor1.getValue();
		assertEquals("JSON VALUE", textMessageValue2.getPayload());
	}
}