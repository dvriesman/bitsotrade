package com.github.dvriesman.bitsotrade.provider;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrder;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrderPayload;
import com.github.dvriesman.bitsotrade.model.rest.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import com.github.dvriesman.bitsotrade.service.websocket.WebsocketEndpoint;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class OrderBookDataProvider {

    private BigInteger currentSequence;

    @Autowired
    private RestClientFacade restClientFacade;

    protected ListProperty<BookEntity> asks = new SimpleListProperty<>();
    protected ListProperty<BookEntity> bids = new SimpleListProperty<>();


    public ListProperty<BookEntity> getAsks() {
        return asks;
    }

    public ListProperty<BookEntity> getBids() {
        return bids;
    }

    public void init() {
        OrderBookResponse orderBook = restClientFacade.getOrderBook();
        currentSequence = orderBook.getPayload().getSequence();
        asks.set(FXCollections.observableArrayList(orderBook.getPayload().getAsks()));
        bids.set(FXCollections.observableArrayList(orderBook.getPayload().getBids()));
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
