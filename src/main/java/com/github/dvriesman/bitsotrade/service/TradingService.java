package com.github.dvriesman.bitsotrade.service;

import com.github.dvriesman.bitsotrade.Constants;
import com.github.dvriesman.bitsotrade.cloud.rest.RestClientFacade;
import com.github.dvriesman.bitsotrade.components.TradingStrategy;
import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import com.github.dvriesman.bitsotrade.model.domain.TradesResponse;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/***
 * Service responsible to handle Trades
 */
@Service
public class TradingService {

    @Autowired
    private RestClientFacade restClientFacade;

    @Autowired
    private TradingStrategy tradingStrategy;

    private StringProperty tradeSizeLimitProperty = new SimpleStringProperty();
    private StringProperty upticketLimitProperty = new SimpleStringProperty();
    private StringProperty downticketLimitProperty = new SimpleStringProperty();

    /***
     * Update trades using REST (as request in the exercise to pool and not use websockets).
     * Interval is 1 second.
     */
    @Scheduled(fixedRate = 2000)
    public void updateTrade() {
        final TradesResponse tradeResponse = restClientFacade.getTrades(getLimit());
        final List<TradesPayload> tradesPayloads = tradingStrategy.runStrategy(tradeResponse.getPayload(), getUpticketCount(), getDownticketCount());
        Platform.runLater(() -> {
            final List<TradesPayload> listToShow = tradesPayloads.stream().limit(getLimit()).collect(Collectors.toList());
            trades.set(FXCollections.observableArrayList(listToShow));
        });
    }

    private Integer getLimit() {
        return tradeSizeLimitProperty.get() != null &&
                tradeSizeLimitProperty.get().trim().length() > 0 ?
                new Integer(tradeSizeLimitProperty.get()) : Constants.DEFAULT_LIMIT;
    }
    private Integer getUpticketCount() {
        return upticketLimitProperty.get() != null &&
                upticketLimitProperty.get().trim().length() > 0 ?
                new Integer(upticketLimitProperty.get()) : Constants.DEFAULT_UPTICKET;
    }
    private Integer getDownticketCount() {
        return downticketLimitProperty.get() != null &&
                downticketLimitProperty.get().trim().length() > 0 ?
                new Integer(downticketLimitProperty.get()) : Constants.DEFAULT_DOWNTICKET;
    }

    public StringProperty getTradeSizeLimitPropertyProperty() {
        return tradeSizeLimitProperty;
    }

    public StringProperty getUpticketLimitProperty() {
        return upticketLimitProperty;
    }

    public StringProperty getDownticketLimitProperty() {
        return downticketLimitProperty;
    }

    private ListProperty<TradesPayload> trades = new SimpleListProperty<>();

    public ListProperty<TradesPayload> getTrades() {
        return trades;
    }
}
