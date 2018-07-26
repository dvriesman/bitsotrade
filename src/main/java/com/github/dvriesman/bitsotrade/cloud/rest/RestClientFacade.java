package com.github.dvriesman.bitsotrade.cloud.rest;

import com.github.dvriesman.bitsotrade.Constants;
import com.github.dvriesman.bitsotrade.model.domain.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.domain.TradesResponse;
import com.github.dvriesman.bitsotrade.cloud.rest.api.OrderBookService;
import com.github.dvriesman.bitsotrade.cloud.rest.api.TradeService;
import com.github.dvriesman.bitsotrade.cloud.rest.util.RetrofitClientBuilder;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class RestClientFacade {

    private OrderBookService orderBookService;
    private TradeService tradeService;

    public RestClientFacade() {
        orderBookService = RetrofitClientBuilder.createService(OrderBookService.class, Constants.BASE_URL);
        tradeService = RetrofitClientBuilder.createService(TradeService.class, Constants.BASE_URL);
    }

    public OrderBookResponse getOrderBook() {
        Call<OrderBookResponse> call = orderBookService.getOrderBook(Constants.BOOK, false);
        try {
            Response<OrderBookResponse> resp = call.execute();
            OrderBookResponse body = resp.body();
            return body;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TradesResponse getTrades(Integer limit) {
        Call<TradesResponse> call = tradeService.getTrades(Constants.BOOK, limit);
        try {
            Response<TradesResponse> resp = call.execute();
            TradesResponse body = resp.body();
            return body;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}
