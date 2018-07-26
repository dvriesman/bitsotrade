package com.github.dvriesman.bitsotrade.controller;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;
import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import com.github.dvriesman.bitsotrade.service.OrderBookService;
import com.github.dvriesman.bitsotrade.service.TradingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private OrderBookService orderBookService;

    @Autowired
    private TradingService tradingService;

    @FXML
    private ListView<BookEntity> asksListView;

    @FXML
    private ListView<BookEntity> bidsListView;

    @FXML
    private TableView<TradesPayload> tradesTableView;

    @FXML
    private TextField orderBookSizeLimit;

    @FXML
    private TextField tradeSizeLimit;

    public void initialize(URL location, ResourceBundle resourceBundle) {

        handleIntegerField(tradeSizeLimit);
        handleIntegerField(orderBookSizeLimit);

        bindEveryOne();

        orderBookSizeLimit.setText(BOOK_SIZE_DEFAULT_LIMIT);
        tradeSizeLimit.setText(TRADE_SIZE_DEFAULT_LIMIT);

        orderBookService.init();

    }

    private void handleIntegerField(final TextField tf) {
        tf.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }));
    }

    private void bindEveryOne() {
        orderBookSizeLimit.textProperty().bindBidirectional(orderBookService.getOrderBookSizeLimitPropertyProperty());
        tradeSizeLimit.textProperty().bindBidirectional(tradingService.getTradeSizeLimitPropertyProperty());
        asksListView.itemsProperty().bind(orderBookService.getAsks());
        bidsListView.itemsProperty().bind(orderBookService.getBids());
        tradesTableView.itemsProperty().bind(tradingService.getTrades());
    }


}
