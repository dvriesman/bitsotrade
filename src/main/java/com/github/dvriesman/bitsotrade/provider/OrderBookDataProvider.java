package com.github.dvriesman.bitsotrade.provider;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrder;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrderPayload;
import com.github.dvriesman.bitsotrade.model.rest.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import com.github.dvriesman.bitsotrade.service.websocket.WebsocketEndpoint;
import javafx.application.Platform;

import java.math.BigInteger;
import java.util.List;

public class OrderBookDataProvider {

    private static OrderBookDataProvider instance;

    private BigInteger currentSequence;

    private List<BookEntity> asks;
    private List<BookEntity> bids;

    public List<BookEntity> getAsks() {
        return asks;
    }

    public void setAsks(List<BookEntity> asks) {
        this.asks = asks;
    }

    public List<BookEntity> getBids() {
        return bids;
    }

    public void setBids(List<BookEntity> bids) {
        this.bids = bids;
    }

    private OrderBookDataProvider() {

        OrderBookResponse orderBook = RestClientFacade.getInstance().getOrderBook();
        currentSequence = orderBook.getPayload().getSequence();
        asks = orderBook.getPayload().getAsks();
        bids = orderBook.getPayload().getBids();
    }

    public static OrderBookDataProvider getOrderBookDataProvider() {
        return instance == null ? (instance = new OrderBookDataProvider()) : instance;
    }

    public void updateOrderBook(DiffOrder diffOrder) {
        if (diffOrder != null) {
            if (diffOrder.getSequence().compareTo(currentSequence) > 0) {
                List<DiffOrderPayload> payload = diffOrder.getPayload();
                if (payload != null) {
                    payload.stream().forEach(e -> updateBook(e));
                    currentSequence = diffOrder.getSequence();
                }
            }
        }
    }

    private void updateBook(DiffOrderPayload e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (e.getStatus()) {
                    case CANCELLED: {
                        if (e.getType().equals(OpTypeEnum.BUY)) {
                            asks.remove(new BookEntity(e.getId()));
                        } else {
                            bids.remove(new BookEntity(e.getId()));
                        }
                        System.out.println("Cancelled!");
                    }

                    case OPEN: {
                        if (e.getType().equals(OpTypeEnum.BUY)) {
                            asks.add(new BookEntity(e.getId(), "btc_mxn", e.getValue(), e.getAmount()));
                        } else {
                            bids.add(new BookEntity(e.getId(), "btc_mxn", e.getValue(), e.getAmount()));
                        }
                        System.out.println("Open!");
                    }
                }

            }
        });
    }


}
