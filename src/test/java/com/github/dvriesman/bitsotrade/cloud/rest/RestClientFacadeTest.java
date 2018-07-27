package com.github.dvriesman.bitsotrade.cloud.rest;

import com.github.dvriesman.bitsotrade.model.domain.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.domain.TradesResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
public class RestClientFacadeTest {

    @InjectMocks
    private RestClientFacade restClientFacade;

    @Test
    public void getOrderBookTest() {
        OrderBookResponse orderBook = restClientFacade.getOrderBook();
        assertTrue(orderBook != null);
        assertTrue(orderBook.getSuccess());
        assertTrue(orderBook.getPayload() != null);
        assertTrue(orderBook.getPayload().getAsks().size() > 0);
        assertTrue(orderBook.getPayload().getBids().size() > 0);
    }

    @Test
    public void getTradesTest() {
        TradesResponse trades = restClientFacade.getTrades(10);
        assertTrue(trades != null);
        assertTrue(trades.getSuccess());
        assertTrue(trades.getPayload() != null);
        assertTrue(trades.getPayload().size() <= 10);
    }


}
