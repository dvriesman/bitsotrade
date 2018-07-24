package com.github.dvriesman.bitsotrade.provider;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.rest.TradesPayload;
import com.github.dvriesman.bitsotrade.model.rest.TradesResponse;
import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TradingDataProvider {

    @Autowired
    private RestClientFacade restClientFacade;

    private Integer limit;

    private ListProperty<TradesPayload> trades = new SimpleListProperty<>();

    public ListProperty<TradesPayload> getTrades() {
        return trades;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void init(Integer limit) {
        setLimit(limit);
    }

    @Scheduled(fixedRate = 10000)
    public void updateTrade() {
        if (limit != null) {
            TradesResponse tradeResponse = restClientFacade.getTrades(limit);
            trades.set(FXCollections.observableArrayList(tradeResponse.getPayload()));
        }
    }


}
