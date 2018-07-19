package com.github.dvriesman.bitsotrade.model.rest;

public class OrderBookResponse {

    private Boolean success;
    private OrderBookPayload orderBookPayload;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public OrderBookPayload getOrderBookPayload() {
        return orderBookPayload;
    }

    public void setOrderBookPayload(OrderBookPayload orderBookPayload) {
        this.orderBookPayload = orderBookPayload;
    }
}
