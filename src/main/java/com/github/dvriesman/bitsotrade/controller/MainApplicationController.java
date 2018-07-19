package com.github.dvriesman.bitsotrade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainApplicationController implements Initializable {

    @FXML
    private ListView<String> bidList;

    private Button btnTest;

    private ObservableList<String> items =FXCollections.observableArrayList (
            "Single", "Double", "Suite", "Family App");

    @FXML
    protected void onClick(ActionEvent event) {
        items.add("buceta");

    }
    public void initialize(URL location, ResourceBundle resourceBundle) {


        bidList.setItems(items);
    }


}
