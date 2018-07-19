package com.github.dvriesman.bitsotrade.model.rest;

import com.github.dvriesman.bitsotrade.model.domain.BookEntity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class OrderBookPayload {

    private List<BookEntity> asks;
    private List<BookEntity> bids;

    Date updatedAt;

    BigInteger sequence;

    public List<BookEntity> getAsks() {
        return asks;
    }

    public void setAsks(List<BookEntity> asks) {
        this.asks = asks;
    }

    public List<BookEntity> getBids() {
        return bids;
    }

    public void setBids(List<BookEntity> bids) {
        this.bids = bids;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigInteger getSequence() {
        return sequence;
    }

    public void setSequence(BigInteger sequence) {
        this.sequence = sequence;
    }
}
