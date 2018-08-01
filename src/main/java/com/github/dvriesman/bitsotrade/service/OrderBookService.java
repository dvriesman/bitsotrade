package com.github.dvriesman.bitsotrade.service;

import com.github.dvriesman.bitsotrade.Constants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * Service responsible handle OrderBook
 */
@Service
public class OrderBookService {

    Logger logger = LoggerFactory.getLogger(OrderBookService.class);

    private BigInteger currentSequence;

    private Queue<DiffOrder> processQueue = new ConcurrentLinkedQueue<>();

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
                new Integer(orderBookSizeLimitProperty.get()) : Constants.DEFAULT_LIMIT ;

        return limit;
    }

    Function<List<BookEntity>, List<BookEntity>> defaultSortList = x->
            x.stream().sorted(Comparator.comparing(BookEntity::getPrice)).limit(getLimit(x)).collect(Collectors.toList());

    Function<List<BookEntity>, List<BookEntity>> reversedSortList = x->
            x.stream().sorted(Comparator.comparing(BookEntity::getPrice).reversed())
                    .limit(getLimit(x)).collect(Collectors.toList());

    /***
     * Init order book using rest call
     */
    public void init() {
        OrderBookResponse orderBook = restClientFacade.getOrderBook();
        currentSequence = orderBook.getPayload().getSequence();
        askList = orderBook.getPayload().getAsks();
        bidList = orderBook.getPayload().getBids();
        asks.set(FXCollections.observableArrayList(defaultSortList.apply(askList)));
        bids.set(FXCollections.observableArrayList(reversedSortList.apply(bidList)));
    }

    /***
     * Update order book
     * @param diffOrder DiffOrder datastructure
     */
    public void updateOrderBook(DiffOrder diffOrder) {
        logger.debug("INTO LIST: " + (diffOrder != null ? diffOrder.getSequence() : "(diffOrder is null)"));
        processQueue.offer(diffOrder);
    }

    @Scheduled(fixedRate = 1000)
    private void scheduleUpdateBooks() {
        DiffOrder diffOrder = null;
        if (currentSequence != null) {
            while((diffOrder = processQueue.poll()) != null) {
                logger.debug("GET OUT FROM LIST: " + diffOrder.getSequence());
                if (diffOrder.getSequence() != null && diffOrder.getSequence().compareTo(currentSequence) > 0) {
                    logger.debug("SYNC: " + diffOrder.getSequence());
                    List<DiffOrderPayload> payload = diffOrder.getPayload();
                    if (payload != null) {
                        currentSequence = diffOrder.getSequence();
                        payload.stream().forEach(e -> updateBook(e));
                    }
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
                    list.add(new BookEntity(e.getId(), Constants.BOOK, e.getRate(), e.getAmount()));
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
                    logger.error("INVALID DIFF ORDER STATUS RECEIVED!");
                    break;
                }
            }
        });
    }
}
