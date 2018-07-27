package com.github.dvriesman.bitsotrade.components;

import com.github.dvriesman.bitsotrade.model.domain.TradesPayload;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TradingStrategyTest {

    static final long DIFF_BETWEEN_DATES=10;

    @Test
    public void runStrategyBasicUpticketTest() {
        TradingStrategy ts = new TradingStrategy();
        List<TradesPayload> trades = newTestList(10,20,30,40,50);
        List<TradesPayload> tradesPayloads = ts.runStrategy(trades, 3, 8);
        assertTrue(tradesPayloads.get(0).getPrice().equals(tradesPayloads.get(1).getPrice()));
        assertTrue(tradesPayloads.get(0).getMakerSide().equals("sell"));
    }

    @Test
    public void runStrategyBasicWithZeroTicketUpticketTest() {
        TradingStrategy ts = new TradingStrategy();
        List<TradesPayload> trades = newTestList(20,30,40,40,50);
        List<TradesPayload> tradesPayloads = ts.runStrategy(trades, 3, 8);
        assertTrue(tradesPayloads.get(0).getPrice().equals(tradesPayloads.get(1).getPrice()));
        assertTrue(tradesPayloads.get(0).getMakerSide().equals("sell"));
    }

    @Test
    public void runStrategyNoTradeTest() {
        TradingStrategy ts = new TradingStrategy();
        List<TradesPayload> trades = newTestList(40,50,40,40,50);
        List<TradesPayload> tradesPayloads = ts.runStrategy(trades, 4, 8);
        assertTrue(tradesPayloads.size() == trades.size());
    }

    @Test
    public void runStrategyBasicDownticketTest() {
        TradingStrategy ts = new TradingStrategy();
        List<TradesPayload> trades = newTestList(50,40,30,20,10);
        List<TradesPayload> tradesPayloads = ts.runStrategy(trades, 4, 3);
        assertTrue(tradesPayloads.get(0).getAmount().equals(1.0));
        assertTrue(tradesPayloads.get(0).getMakerSide().equals("buy"));
    }

    @Test
    public void runStrategyDownticketWithZeroTicketTest() {
        TradingStrategy ts = new TradingStrategy();
        List<TradesPayload> trades = newTestList(10,50,20,20,10);
        List<TradesPayload> tradesPayloads = ts.runStrategy(trades, 4, 2);
        assertTrue(tradesPayloads.get(0).getAmount().equals(1.0));
        assertTrue(tradesPayloads.get(0).getMakerSide().equals("buy"));
    }

    @Test
    public void runStrategyLongDownticketWithZeroTicketTest() {
        TradingStrategy ts = new TradingStrategy();
        List<TradesPayload> trades = newTestList(70,60,60,50,45,35,30,30,20,20,10);
        List<TradesPayload> tradesPayloads = ts.runStrategy(trades, 4, 6);
        assertTrue(tradesPayloads.get(0).getAmount().equals(1.0));
        assertTrue(tradesPayloads.get(0).getMakerSide().equals("buy"));
    }


    private List<TradesPayload> newTestList(double... values) {
        List<TradesPayload> result = new LinkedList<>();
        TradesPayload trade = null;
        Date date = new Date();
        for (double value : values) {
            trade = new TradesPayload();
            trade.setAmount(0.010);
            trade.setPrice(value);
            date = new Date(date.getTime() + (DIFF_BETWEEN_DATES));
            trade.setCreatedAt(date);
            result.add(trade);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}
