package com.github.dvriesman.bitsotrade.cloud.rest.api;

import com.github.dvriesman.bitsotrade.model.domain.TradesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/***
 * Abstraction of trades call
 */
public interface TradeService {

    @GET("trades")
    Call<TradesResponse> getTrades(@Query("book") String book, @Query("limit") Integer limit);


}
