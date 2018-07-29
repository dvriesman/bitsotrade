package com.github.dvriesman.bitsotrade.cloud.websocket;


import com.github.dvriesman.bitsotrade.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/***
 * Websocket setup
 */
@Configuration
public class WebSocketConfiguration {

    @Bean
    public WebSocketConnectionManager connectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client(), handler(), Constants.WS_URI);
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public StandardWebSocketClient client() {
        return new StandardWebSocketClient();
    }

    @Bean
    public OrderBookWebSocketHandler handler() {
        return new OrderBookWebSocketHandler();
    }

}