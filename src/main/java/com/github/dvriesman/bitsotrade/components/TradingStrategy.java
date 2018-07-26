package com.github.dvriesman.bitsotrade.components;

import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import org.springframework.stereotype.Component;

@Component
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
