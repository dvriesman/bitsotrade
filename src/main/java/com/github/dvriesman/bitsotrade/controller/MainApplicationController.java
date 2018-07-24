package com.github.dvriesman.bitsotrade.controller;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.rest.TradesPayload;
import com.github.dvriesman.bitsotrade.provider.OrderBookDataProvider;
import com.github.dvriesman.bitsotrade.provider.TradingDataProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainApplicationController implements Initializable {

    @Autowired
    private OrderBookDataProvider orderBookDataProvider;

    @Autowired
    private TradingDataProvider tradingDataProvider;

    @FXML
    private ListView<BookEntity> asksListView;

    @FXML
    private ListView<BookEntity> bidsListView;

    @FXML
    private TableView<TradesPayload> tradesTableView;

    @FXML
    private Button btnTest;



    @FXML
    protected void onClick(ActionEvent event) {


    }

    public void initialize(URL location, ResourceBundle resourceBundle) {

        orderBookDataProvider.init();

        tradingDataProvider.init(50);

        asksListView.itemsProperty().bind(orderBookDataProvider.getAsks());

        bidsListView.itemsProperty().bind(orderBookDataProvider.getBids());

        tradesTableView.itemsProperty().bind(tradingDataProvider.getTrades());

    }


}
