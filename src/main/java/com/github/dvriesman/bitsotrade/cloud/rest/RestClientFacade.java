package com.github.dvriesman.bitsotrade.cloud.rest;

import com.github.dvriesman.bitsotrade.Constants;
import com.github.dvriesman.bitsotrade.model.domain.OrderBookResponse;
import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import com.github.dvriesman.bitsotrade.model.domain.TradesResponse;
import com.github.dvriesman.bitsotrade.cloud.rest.api.OrderBookService;
import com.github.dvriesman.bitsotrade.cloud.rest.api.TradeService;
import com.github.dvriesman.bitsotrade.cloud.rest.util.RetrofitClientBuilder;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * Facade of all rest calls to bitsotrade
 */
public class RestClientFacade {

    private OrderBookService orderBookService;
    private TradeService tradeService;

    /**
     * Constructor that create Retrofit Abstraction APIS
     */
    public RestClientFacade() {
        orderBookService = RetrofitClientBuilder.createService(OrderBookService.class, Constants.BASE_URL);
        tradeService = RetrofitClientBuilder.createService(TradeService.class, Constants.BASE_URL);
    }

    /***
     * Request a full OrderBook
     * @return OrderBookResponse Full Order Book datastructure
     */
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

    /***
     * Request X trades
     * @param limit Number of trades to get
     * @return TradesResponse Trades datastructure
     */
    public TradesResponse getTrades(Integer limit) {
        List<TradesPayload> resultList = new ArrayList<>();
        TradesResponse result = new TradesResponse();
        result.setSuccess(false);
        int callTimes = limit > 100 ? (limit / 100)+1 : 1;
        String latestId = null;
        for (int i = 0; i < callTimes; i++) {
            Call<TradesResponse> call = null;
            if (i == 0) {
                call = tradeService.getTrades(Constants.BOOK, limit > 100 ? 100 : limit);
            } else {
                call = tradeService.getTrades(Constants.BOOK, 100, latestId);
            }
            try {
                Response<TradesResponse> resp = call.execute();
                TradesResponse partialResult = resp.body();
                resultList.addAll(partialResult.getPayload());
                latestId = partialResult.getPayload().get(partialResult.getPayload().size()-1).getTid();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
        result.setPayload(resultList);
        result.setSuccess(true);
        return result;
    }

}
