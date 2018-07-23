package com.github.dvriesman.bitsotrade.provider;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrder;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrderPayload;
import com.github.dvriesman.bitsotrade.model.rest.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderBookDataProvider {

    private BigInteger currentSequence;

    @Autowired
    private RestClientFacade restClientFacade;

    protected ListProperty<BookEntity> asks = new SimpleListProperty<>();
    protected ListProperty<BookEntity> bids = new SimpleListProperty<>();

    private List<BookEntity> askList;
    private List<BookEntity> bidList;


    public ListProperty<BookEntity> getAsks() {
        return asks;
    }

    public ListProperty<BookEntity> getBids() {
        return bids;
    }

    private List<BookEntity> getSortList(List<BookEntity> list) {
        try {
            return list.stream().sorted(Comparator.comparing(BookEntity::getPrice)
            ).limit(20).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(list);
            e.printStackTrace();
        }
        return null;
    }

    public void init() {
        OrderBookResponse orderBook = restClientFacade.getOrderBook();
        currentSequence = orderBook.getPayload().getSequence();

        askList = orderBook.getPayload().getAsks();

        bidList = orderBook.getPayload().getBids();

        asks.set(FXCollections.observableArrayList(getSortList(askList)));
        bids.set(FXCollections.observableArrayList(getSortList(bidList)));

    }


    public void updateOrderBook(DiffOrder diffOrder) {
        if (currentSequence != null && diffOrder != null) {
            if (diffOrder.getSequence() != null && diffOrder.getSequence().compareTo(currentSequence) > 0) {
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
                            askList.remove(new BookEntity(e.getId()));
                            asks.set(FXCollections.observableArrayList(getSortList(askList)));
                        } else {
                            bidList.remove(new BookEntity(e.getId()));
                            bids.set(FXCollections.observableArrayList(getSortList(bidList)));
                        }
                        System.out.println("Cancelled!");
                        break;
                    }

                    case OPEN: {
                        if (e.getType().equals(OpTypeEnum.BUY)) {

                            askList.add(new BookEntity(e.getId(), "btc_mxn", e.getValue(), e.getAmount()));
                            asks.set(FXCollections.observableArrayList(getSortList(askList)));

                        } else {
                            bidList.add(new BookEntity(e.getId(), "btc_mxn", e.getValue(), e.getAmount()));
                            bids.set(FXCollections.observableArrayList(getSortList(bidList)));
                        }
                        System.out.println("Open!");
                        break;
                    }
                }

            }
        });
    }


}
