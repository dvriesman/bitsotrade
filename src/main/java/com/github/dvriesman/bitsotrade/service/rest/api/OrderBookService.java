package com.github.dvriesman.bitsotrade.service.rest.api;

import com.github.dvriesman.bitsotrade.model.rest.OrderBookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderBookService {

    @GET("order_book")
    Call<OrderBookResponse> getOrderBook(@Query("book") String book, @Query("aggregate") Boolean aggregate);

}
