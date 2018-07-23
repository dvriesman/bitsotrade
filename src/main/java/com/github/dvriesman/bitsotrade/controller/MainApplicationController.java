package com.github.dvriesman.bitsotrade.controller;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.provider.OrderBookDataProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
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


    @FXML
    protected void onClick(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resourceBundle) {

        asksListProperty.set(FXCollections.observableArrayList(OrderBookDataProvider.getOrderBookDataProvider().getAsks()));
        asksListView.itemsProperty().bind(asksListProperty);

        bidsListProperty.set(FXCollections.observableArrayList(OrderBookDataProvider.getOrderBookDataProvider().getBids()));
        bidsListView.itemsProperty().bind(bidsListProperty);

    }


}
