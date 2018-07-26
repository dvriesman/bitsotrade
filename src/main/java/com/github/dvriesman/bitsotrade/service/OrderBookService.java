package com.github.dvriesman.bitsotrade.service;

import com.github.dvriesman.bitsotrade.cloud.rest.RestClientFacade;
import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrder;
import com.github.dvriesman.bitsotrade.model.domain.DiffOrderPayload;
import com.github.dvriesman.bitsotrade.model.domain.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderBookService {

    private static final String DEFAULT_BOOK = "btc_mxn";
    private static final Integer DEFAULT_LIMIT = 20;
    private BigInteger currentSequence;

    @Autowired
    private RestClientFacade restClientFacade;

    private ListProperty<BookEntity> asks = new SimpleListProperty<>();
    private ListProperty<BookEntity> bids = new SimpleListProperty<>();

    private List<BookEntity> askList;
    private List<BookEntity> bidList;

    private StringProperty orderBookSizeLimitProperty = new SimpleStringProperty();

    public StringProperty getOrderBookSizeLimitPropertyProperty() {
        return orderBookSizeLimitProperty;
    }

    public ListProperty<BookEntity> getAsks() {
        return asks;
    }

    public ListProperty<BookEntity> getBids() {
        return bids;
    }

    private Integer getLimit(List<BookEntity> list) {
        Integer limit = orderBookSizeLimitProperty.get() != null &&
                orderBookSizeLimitProperty.get().trim().length() > 0 ?
                new Integer(orderBookSizeLimitProperty.get()) : DEFAULT_LIMIT ;

        return limit;
    }

    Function<List<BookEntity>, List<BookEntity>> defaultSortList = x->
            x.stream().sorted(Comparator.comparing(BookEntity::getPrice)).limit(getLimit(x)).collect(Collectors.toList());

    Function<List<BookEntity>, List<BookEntity>> reversedSortList = x->
            x.stream().sorted(Comparator.comparing(BookEntity::getPrice).reversed())
                    .limit(getLimit(x)).collect(Collectors.toList());

    public void init() {
        OrderBookResponse orderBook = restClientFacade.getOrderBook();
        currentSequence = orderBook.getPayload().getSequence();

        askList = orderBook.getPayload().getAsks();
        bidList = orderBook.getPayload().getBids();

        asks.set(FXCollections.observableArrayList(defaultSortList.apply(askList)));
        bids.set(FXCollections.observableArrayList(reversedSortList.apply(bidList)));

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
        Platform.runLater(() -> {
            List<BookEntity> list = null;
            ListProperty<BookEntity> listProperty = null;
            Function<List<BookEntity>, List<BookEntity>> sortList = null;
            if (e.getType().equals(OpTypeEnum.BUY)) {
                list =askList;
                listProperty = asks;
                sortList = defaultSortList;
            } else {
                list =bidList;
                listProperty = bids;
                sortList = reversedSortList;
            }

            switch (e.getStatus()) {
                case COMPLETED:
                case CANCELLED: {
                    list.remove(new BookEntity(e.getId()));
                    listProperty.set(FXCollections.observableArrayList(sortList.apply(list)));
                    break;
                }
                case OPEN: {
                    list.add(new BookEntity(e.getId(), DEFAULT_BOOK, e.getRate(), e.getAmount()));
                    listProperty.set(FXCollections.observableArrayList(sortList.apply(list)));
                    break;
                }
                case PARTIALLY: {
                    int index = list.indexOf(new BookEntity(e.getId()));
                    BookEntity order = list.get(index);
                    order.setAmount(e.getAmount());
                    order.setPrice(e.getRate());
                    listProperty.set(FXCollections.observableArrayList(sortList.apply(list)));
                    break;
                }
                default: {
                    System.err.println("TODO - LOG4J");
                    break;
                }
            }
        });
    }
}
