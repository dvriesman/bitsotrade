package com.github.dvriesman.bitsotrade.service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrder;
import com.github.dvriesman.bitsotrade.provider.OrderBookDataProvider;
import org.glassfish.tyrus.client.ClientManager;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;


@ClientEndpoint
public class WebsocketEndpoint {

    private static String ENDPOINT_URI = "wss://ws.bitso.com";
    private static CountDownLatch latch;

    @OnOpen
    public void onOpen(Session session) {
        try {
            final JSONObject jsonReq = new JSONObject();
            jsonReq.put("action", "subscribe");
            jsonReq.put("book", "btc_mxn");
            jsonReq.put("type", "diff-orders");
            session.getBasicRemote().sendText(jsonReq.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.indexOf("subscribe") <= 0) {
            try {
                DiffOrder diffOrder = new ObjectMapper().readerFor(DiffOrder.class).readValue(message);
                OrderBookDataProvider.getOrderBookDataProvider().updateOrderBook(diffOrder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        latch.countDown();
    }

    public static void startWebSocket() {
        new Thread() {
            @Override
            public void run() {
                latch = new CountDownLatch(1);
                ClientManager client = ClientManager.createClient();
                try {
                    client.connectToServer(WebsocketEndpoint.class, new URI(ENDPOINT_URI));
                    latch.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }


}
