package com.github.dvriesman.bitsotrade.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrder;
import com.github.dvriesman.bitsotrade.provider.OrderBookDataProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class OrderBookWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;

    @Autowired
    private OrderBookDataProvider orderBookDataProvider;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        final JSONObject jsonReq = new JSONObject();
        jsonReq.put("action", "subscribe");
        jsonReq.put("book", "btc_mxn");
        jsonReq.put("type", "diff-orders");
        this.session.sendMessage(new TextMessage(jsonReq.toString()));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message != null && !message.getPayload().contains("action")) {
            try {
                DiffOrder diffOrder = new ObjectMapper().readerFor(DiffOrder.class).readValue(message.getPayload());
                orderBookDataProvider.updateOrderBook(diffOrder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

    }

    public WebSocketSession getSession() {
        return session;
    }



}
