package com.github.dvriesman.bitsotrade.components;

import com.github.dvriesman.bitsotrade.Constants;
import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TradingStrategy {

    private List<TradesPayload> virtualOrders = new ArrayList<>();

    private int countTickets(List<TradesPayload> toEvaluate, Integer uptickets, Integer downtickets) {
        int tickets = 0;
        TradesPayload latestTrade = null;
        int count = 0;
        for (TradesPayload trade : toEvaluate) {
            count++;
            if (latestTrade != null) {
                if (trade.getPrice().compareTo(latestTrade.getPrice()) > 0) {
                    tickets++;
                } else {
                    if (trade.getPrice().compareTo(latestTrade.getPrice()) < 0) {
                        tickets--;
                    }
                }
                if (tickets >= uptickets || tickets <= (-1 * downtickets)) {
                    break;
                }
            }
            latestTrade = trade;
            if (count > uptickets && count > downtickets) {
                break;
            }
        }
        return tickets;
    }



    public List<TradesPayload> runStrategy(List<TradesPayload> trades, int uptickets, int downtickets) {

        trades.addAll(virtualOrders);

        List<TradesPayload>  toEvaluate = trades.stream()
                    .sorted(Comparator.comparing(TradesPayload::getCreatedAt)
                            .reversed()).collect(Collectors.toList());

        if (toEvaluate.size() > 0) {

            TradesPayload mostRecentlyTrade = toEvaluate.get(0);

            int tickets = countTickets(toEvaluate, uptickets, downtickets);
            TradesPayload newTrade = createExecutedTrade(tickets, uptickets, downtickets,  mostRecentlyTrade.getPrice());

            if (newTrade != null) {
                virtualOrders.add(newTrade);
                trades.add(newTrade);
            }
        }

        return trades.stream().sorted(Comparator.comparing(TradesPayload::getCreatedAt).
                reversed()).collect(Collectors.toList());

    }

    private TradesPayload createExecutedTrade(int tickets, int uptickets, int downtickets, double price) {
        TradesPayload newTrade = null;
        if (tickets >= uptickets) {
            newTrade = createOrder(OpTypeEnum.BUY, price);
        } else {
            if (tickets <= (-1 * downtickets)) {
                newTrade = createOrder(OpTypeEnum.SELL, price);
            }
        }
        return newTrade;
    }


    private TradesPayload createOrder(OpTypeEnum type, Double price) {
        TradesPayload  result = new TradesPayload();
        result.setAmount(1.0);
        result.setBook(Constants.BOOK);
        result.setCreatedAt(new Date());
        result.setMarkerSide(type.equals(OpTypeEnum.BUY) ? "buy" : "sell");
        result.setPrice(price);
        result.setTid("MYSELF");
        result.setVirtual(true);
        return result;
    }

}
