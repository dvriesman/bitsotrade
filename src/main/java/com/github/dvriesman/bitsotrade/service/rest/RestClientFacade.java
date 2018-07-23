package com.github.dvriesman.bitsotrade.service.rest;

import com.github.dvriesman.bitsotrade.model.rest.OrderBookResponse;
import com.github.dvriesman.bitsotrade.service.rest.api.OrderBookService;
import com.github.dvriesman.bitsotrade.service.rest.util.RetrofitClientBuilder;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class RestClientFacade {

    private static final String BASE_URL = "https://api.bitso.com/v3/";
    private static final String FIXED_CURRENCY = "btc_mxn";

    private OrderBookService orderBookService;

    public RestClientFacade() {
        orderBookService = RetrofitClientBuilder.createService(OrderBookService.class, BASE_URL);
    }

    public OrderBookResponse getOrderBook() {
        Call<OrderBookResponse> call = orderBookService.getOrderBook(FIXED_CURRENCY, false);
        try {
            Response<OrderBookResponse> resp = call.execute();
            OrderBookResponse body = resp.body();
            return body;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
