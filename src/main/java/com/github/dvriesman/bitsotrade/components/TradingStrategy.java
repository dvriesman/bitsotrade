package com.github.dvriesman.bitsotrade.components;

import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TradingStrategy {

    private List<TradesPayload> virtualOrders = new ArrayList<>();

    public List<TradesPayload> runStrategy(List<TradesPayload> trades, Integer uptickets, Integer downtickets) {

        System.out.println("Virtual orders: " + virtualOrders.size());

        trades.addAll(virtualOrders);

        List<TradesPayload>  toEvaluate = trades.stream()
                    .sorted(Comparator.comparing(TradesPayload::getCreatedAt).reversed()).collect(Collectors.toList());

        int tickets = 0;

        TradesPayload latestTrade = null;
        TradesPayload mostRecentlyTrade = null;

        int count = 0;
        //To remember - list is inverted (latest by date are coming first).
        System.out.println("Tickets starting  List size: " + toEvaluate.size());
        if (toEvaluate.size() >0) {
            for (TradesPayload trade : toEvaluate) {
                count++;
                System.out.println("Tickets count: " + count);
                if (latestTrade != null) {
                    if (trade.getPrice().compareTo(latestTrade.getPrice()) > 0) {
                        tickets++;
                    } else {
                        if (trade.getPrice().compareTo(latestTrade.getPrice()) < 0) {
                            tickets--;
                        }
                    }
                    if (tickets >= uptickets || tickets <= (-1 * downtickets)) {
                        System.out.println("Breaking: " + tickets + "Downtickets: " + downtickets + "UpTickets: " + uptickets);
                        break;
                    }
                } else {
                    mostRecentlyTrade = trade;
                }
                latestTrade = trade;
                if (count > uptickets && count > downtickets) {
                    System.out.println("Breaking X - count: " + count + " Ticktes: " + tickets + " Downtickets: " + downtickets +  "UpTickets: " + uptickets);
                    break;
                }
            }
            TradesPayload newTrade = null;

            System.out.println("Total tickets: " + tickets);
            if (tickets >= uptickets) {
                newTrade = createOrder(OpTypeEnum.SELL, mostRecentlyTrade.getPrice());
            } else {
                if (tickets <= (-1 * downtickets)) {
                    newTrade = createOrder(OpTypeEnum.BUY, mostRecentlyTrade.getPrice());
                }
            }

            if (newTrade != null) {
                System.out.println("Virtual");
                virtualOrders.add(newTrade);
                trades.add(newTrade);
            }
        }

        return trades.stream().sorted(Comparator.comparing(TradesPayload::getCreatedAt).
                reversed()).collect(Collectors.toList());

    }

    private TradesPayload createOrder(OpTypeEnum type, Double price) {
        TradesPayload  result = new TradesPayload();
        result.setAmount(1.0);
        result.setBook("btc_mxn");
        result.setCreatedAt(new Date());
        result.setMarkerSide(type.equals(OpTypeEnum.BUY) ? "buy" : "sell");
        result.setPrice(price);
        result.setTid("MYSELF");
        result.setVirtual(true);
        return result;
    }

}
