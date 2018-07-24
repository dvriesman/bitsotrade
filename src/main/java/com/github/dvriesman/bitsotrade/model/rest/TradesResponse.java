package com.github.dvriesman.bitsotrade.model.rest;

import java.util.List;

public class TradesResponse {

    private Boolean success;
    private List<TradesPayload> payload;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<TradesPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<TradesPayload> payload) {
        this.payload = payload;
    }
}
