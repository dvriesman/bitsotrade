package com.github.dvriesman.bitsotrade.controller;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.rest.TradesPayload;
import com.github.dvriesman.bitsotrade.provider.OrderBookDataProvider;
import com.github.dvriesman.bitsotrade.provider.TradingDataProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainApplicationController implements Initializable {

    private static final String BOOK_SIZE_DEFAULT_LIMIT = "10";
    private static final String TRADE_SIZE_DEFAULT_LIMIT = "15";


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
    private TextField orderBookSizeLimit;

    @FXML
    private TextField tradeSizeLimit;

    @FXML
    protected void onClick(ActionEvent event) {


    }

    public void initialize(URL location, ResourceBundle resourceBundle) {

        handleIntegerField(tradeSizeLimit);
        handleIntegerField(orderBookSizeLimit);

        bindEveryOne();

        orderBookSizeLimit.setText(BOOK_SIZE_DEFAULT_LIMIT);
        tradeSizeLimit.setText(TRADE_SIZE_DEFAULT_LIMIT);

        orderBookDataProvider.init();

    }

    private void handleIntegerField(final TextField tf) {
        tf.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }));
    }

    private void bindEveryOne() {
        orderBookSizeLimit.textProperty().bindBidirectional(orderBookDataProvider.getOrderBookSizeLimitPropertyProperty());
        tradeSizeLimit.textProperty().bindBidirectional(tradingDataProvider.getTradeSizeLimitPropertyProperty());
        asksListView.itemsProperty().bind(orderBookDataProvider.getAsks());
        bidsListView.itemsProperty().bind(orderBookDataProvider.getBids());
        tradesTableView.itemsProperty().bind(tradingDataProvider.getTrades());
    }


}
