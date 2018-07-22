package com.github.dvriesman.bitsotrade.controller;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.rest.OrderBookResponse;
import com.github.dvriesman.bitsotrade.service.rest.RestClientFacade;
import com.github.dvriesman.bitsotrade.service.websocket.WebsocketEndpoint;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainApplicationController implements Initializable {

    @FXML
    private ListView<BookEntity> asksListView;

    @FXML
    private ListView<BookEntity> bidsListView;

    @FXML
    private Button btnTest;

    protected ListProperty<BookEntity> asksListProperty = new SimpleListProperty<>();
    protected ListProperty<BookEntity> bidsListProperty = new SimpleListProperty<>();

    private List<BookEntity> asks;
    private List<BookEntity> bids;

    @FXML
    protected void onClick(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resourceBundle) {

        OrderBookResponse orderBook = RestClientFacade.getInstance().getOrderBook();
        asks = orderBook.getPayload().getAsks();
        bids = orderBook.getPayload().getBids();


        asksListProperty.set(FXCollections.observableArrayList(asks));
        asksListView.itemsProperty().bind(asksListProperty);

        bidsListProperty.set(FXCollections.observableArrayList(bids));
        bidsListView.itemsProperty().bind(bidsListProperty);

        WebsocketEndpoint.startWebSocket();


    }


}
