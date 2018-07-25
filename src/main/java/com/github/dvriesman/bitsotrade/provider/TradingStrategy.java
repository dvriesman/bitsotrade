package com.github.dvriesman.bitsotrade.provider;

import com.github.dvriesman.bitsotrade.model.rest.TradesPayload;
import org.springframework.stereotype.Service;

@Service
public class TradingStrategy {

    private Integer uptickets;
    private Integer downtickets;

    public void init(Integer uptickets, Integer downtickets) {
        this.uptickets = uptickets;
        this.downtickets = downtickets;
    }

    public TradesPayload runStrategy(TradesPayload trade) {

        return null;

    }



}
