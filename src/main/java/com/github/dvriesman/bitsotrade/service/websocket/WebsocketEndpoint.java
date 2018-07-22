package com.github.dvriesman.bitsotrade.service.websocket;

import org.glassfish.tyrus.client.ClientManager;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;


@ClientEndpoint
public class WebsocketEndpoint {

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
    public String onMessage(String message, Session session) {
        System.out.println(message);
        return "OK";
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
                    client.connectToServer(WebsocketEndpoint.class, new URI("wss://ws.bitso.com"));
                    latch.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();

    }

}
