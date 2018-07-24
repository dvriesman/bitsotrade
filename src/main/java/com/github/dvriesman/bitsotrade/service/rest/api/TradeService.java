package com.github.dvriesman.bitsotrade.service.rest.api;

import com.github.dvriesman.bitsotrade.model.rest.TradesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TradeService {

    @GET("trades")
    Call<TradesResponse> getTrades(@Query("book") String book, @Query("limit") Integer limit);


}
