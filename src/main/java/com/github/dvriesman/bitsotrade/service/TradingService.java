package com.github.dvriesman.bitsotrade.service;

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

@Service
public class TradingService {

    @Autowired
    private RestClientFacade restClientFacade;

    private static final Integer DEFAULT_LIMIT = 20;

    private StringProperty tradeSizeLimitProperty = new SimpleStringProperty();

    public StringProperty getTradeSizeLimitPropertyProperty() {
        return tradeSizeLimitProperty;
    }

    private ListProperty<TradesPayload> trades = new SimpleListProperty<>();

    public ListProperty<TradesPayload> getTrades() {
        return trades;
    }

    private Integer getLimit() {
        return tradeSizeLimitProperty.get() != null &&
                tradeSizeLimitProperty.get().trim().length() > 0 ?
                new Integer(tradeSizeLimitProperty.get()) : DEFAULT_LIMIT;
    }

    @Scheduled(fixedRate = 1000)
    public void updateTrade() {
        TradesResponse tradeResponse = restClientFacade.getTrades(getLimit());
        Platform.runLater(() -> {
            trades.set(FXCollections.observableArrayList(tradeResponse.getPayload()));
        });
    }

}
