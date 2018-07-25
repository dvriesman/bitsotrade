package com.github.dvriesman.bitsotrade.provider;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.rest.TradesPayload;
import com.github.dvriesman.bitsotrade.model.rest.TradesResponse;
import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TradingDataProvider {

    @Autowired
    private RestClientFacade restClientFacade;

    private StringProperty tradeSizeLimitProperty = new SimpleStringProperty();

    public StringProperty getTradeSizeLimitPropertyProperty() {
        return tradeSizeLimitProperty;
    }

    private ListProperty<TradesPayload> trades = new SimpleListProperty<>();

    public ListProperty<TradesPayload> getTrades() {
        return trades;
    }

    private Integer getLimit() {
        return tradeSizeLimitProperty.get() != null && tradeSizeLimitProperty.get().trim().length() > 0 ?
                new Integer(tradeSizeLimitProperty.get()) : 10;
    }

    @Scheduled(fixedRate = 1000)
    public void updateTrade() {
        TradesResponse tradeResponse = restClientFacade.getTrades(getLimit());
        trades.set(FXCollections.observableArrayList(tradeResponse.getPayload()));
    }

}
