package com.github.dvriesman.bitsotrade.controller;

import com.github.dvriesman.bitsotrade.Constants;
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

/***
 * Main application JavaFX Controller
 */
@Component
public class MainApplicationController implements Initializable {

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

    @FXML
    private TextField downTicketCount;

    @FXML
    private TextField upTicketCount;

    /***
     * Do initial setup of screen and binds with controllers
     * @param location
     * @param resourceBundle
     */
    public void initialize(URL location, ResourceBundle resourceBundle) {
        handleIntegerField(tradeSizeLimit);
        handleIntegerField(orderBookSizeLimit);
        handleIntegerField(upTicketCount);
        handleIntegerField(downTicketCount);
        bindEveryOne();
        orderBookSizeLimit.setText(Constants.BOOK_SIZE_DEFAULT_LIMIT);
        tradeSizeLimit.setText(Constants.TRADE_SIZE_DEFAULT_LIMIT);
        upTicketCount.setText(String.valueOf(Constants.DEFAULT_UPTICKET));
        downTicketCount.setText(String.valueOf(Constants.DEFAULT_DOWNTICKET));
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
        upTicketCount.textProperty().bindBidirectional(tradingService.getUpticketLimitProperty());
        downTicketCount.textProperty().bindBidirectional(tradingService.getDownticketLimitProperty());
        asksListView.itemsProperty().bind(orderBookService.getAsks());
        bidsListView.itemsProperty().bind(orderBookService.getBids());
        tradesTableView.itemsProperty().bind(tradingService.getTrades());
    }


}
