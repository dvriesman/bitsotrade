package com.github.dvriesman.bitsotrade.service;

import com.github.dvriesman.bitsotrade.Constants;
import com.github.dvriesman.bitsotrade.components.TradingStrategy;
import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import com.github.dvriesman.bitsotrade.model.domain.TradesResponse;
import com.github.dvriesman.bitsotrade.cloud.rest.RestClientFacade;
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

@Service
public class TradingService {

    @Autowired
    private RestClientFacade restClientFacade;

    @Autowired
    private TradingStrategy tradingStrategy;

    private StringProperty tradeSizeLimitProperty = new SimpleStringProperty();
    private StringProperty upticketLimitProperty = new SimpleStringProperty();
    private StringProperty downticketLimitProperty = new SimpleStringProperty();

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

    private Integer getLimit() {
        return upticketLimitProperty.get() != null &&
                upticketLimitProperty.get().trim().length() > 0 ?
                new Integer(upticketLimitProperty.get()) : Constants.DEFAULT_UPTICKET;
    }
    private Integer getUpticketCount() {
        return tradeSizeLimitProperty.get() != null &&
                tradeSizeLimitProperty.get().trim().length() > 0 ?
                new Integer(tradeSizeLimitProperty.get()) : Constants.DEFAULT_LIMIT;
    }
    private Integer getDownticketCount() {
        return downticketLimitProperty.get() != null &&
                downticketLimitProperty.get().trim().length() > 0 ?
                new Integer(downticketLimitProperty.get()) : Constants.DEFAULT_DOWNTICKET;
    }



    @Scheduled(fixedRate = 1000)
    public void updateTrade() {
        TradesResponse tradeResponse = restClientFacade.getTrades(getLimit());
        List<TradesPayload> tradesPayloads = tradingStrategy.runStrategy(tradeResponse.getPayload(), getUpticketCount(), getDownticketCount());
        Platform.runLater(() -> {
            trades.set(FXCollections.observableArrayList(tradesPayloads));
        });
    }

}
