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

    private ListProperty<BookEntity> asks = new SimpleListProperty<>();
    private ListProperty<BookEntity> bids = new SimpleListProperty<>();

    private List<BookEntity> askList;
    private List<BookEntity> bidList;


    public ListProperty<BookEntity> getAsks() {
        return asks;
    }

    public ListProperty<BookEntity> getBids() {
        return bids;
    }

    private List<BookEntity> getSortList(List<BookEntity> list) {
        return list.stream().sorted(Comparator.comparing(BookEntity::getPrice)
            ).limit(50).collect(Collectors.toList());
    }

    private List<BookEntity> getReversedSortList(List<BookEntity> list) {
        return list.stream().sorted(Comparator.comparing(BookEntity::getPrice).reversed()
        ).limit(50).collect(Collectors.toList());
    }


    public void init() {
        OrderBookResponse orderBook = restClientFacade.getOrderBook();
        currentSequence = orderBook.getPayload().getSequence();

        askList = orderBook.getPayload().getAsks();
        bidList = orderBook.getPayload().getBids();

        asks.set(FXCollections.observableArrayList(getSortList(askList)));
        bids.set(FXCollections.observableArrayList(getReversedSortList(bidList)));

    }


    public void updateOrderBook(DiffOrder diffOrder) {
        if (currentSequence != null && diffOrder != null) {
            if (diffOrder.getSequence() != null && diffOrder.getSequence().compareTo(currentSequence) > 0) {
                List<DiffOrderPayload> payload = diffOrder.getPayload();
                if (payload != null) {
                    currentSequence = diffOrder.getSequence();
                    payload.stream().forEach(e -> updateBook(e));
                }
            }
        }
    }

    private void updateBook(DiffOrderPayload e) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (e.getStatus()) {
                    case COMPLETED:
                    case CANCELLED: {
                        if (e.getType().equals(OpTypeEnum.BUY)) {
                            System.out.println("Cancelled ASK!");
                            askList.remove(new BookEntity(e.getId()));
                            asks.set(FXCollections.observableArrayList(getSortList(askList)));
                        } else {
                            System.out.println("Cancelled BID!");
                            bidList.remove(new BookEntity(e.getId()));
                            bids.set(FXCollections.observableArrayList(getReversedSortList(bidList)));
                        }
                        break;
                    }
                    case OPEN: {
                        if (e.getType().equals(OpTypeEnum.BUY)) {
                            System.out.println("OPEN ASK!");
                            askList.add(new BookEntity(e.getId(), "btc_mxn", e.getRate(), e.getAmount()));
                            asks.set(FXCollections.observableArrayList(getSortList(askList)));
                        } else {
                            System.out.println("OPEN BID");
                            bidList.add(new BookEntity(e.getId(), "btc_mxn", e.getRate(), e.getAmount()));
                            bids.set(FXCollections.observableArrayList(getReversedSortList(bidList)));
                        }
                        System.out.println("Open!");
                        break;
                    }
                    case PARTIALLY: {
                        if (e.getType().equals(OpTypeEnum.BUY)) {
                            System.out.println("Cancelled ASK!");
                            int index = askList.indexOf(new BookEntity(e.getId()));
                            BookEntity order = askList.get(index);
                            order.setAmount(e.getAmount());
                            order.setPrice(e.getRate());
                            asks.set(FXCollections.observableArrayList(getSortList(askList)));
                        } else {
                            System.out.println("Cancelled BID!");
                            int index = bidList.indexOf(new BookEntity(e.getId()));
                            BookEntity order = bidList.get(index);
                            order.setAmount(e.getAmount());
                            order.setPrice(e.getRate());
                            bids.set(FXCollections.observableArrayList(getReversedSortList(bidList)));
                        }
                        break;

                    }
                    default: {
                        System.out.println("error... todo");
                        break;
                    }


                }

            }
        });
    }


}
