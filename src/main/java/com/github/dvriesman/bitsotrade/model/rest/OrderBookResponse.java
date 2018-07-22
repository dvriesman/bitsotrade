package com.github.dvriesman.bitsotrade.model.rest;

public class OrderBookResponse {

    private Boolean success;
    private OrderBookPayload payload;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public OrderBookPayload getPayload() {
        return payload;
    }

    public void setPayload(OrderBookPayload payload) {
        this.payload = payload;
    }
}
