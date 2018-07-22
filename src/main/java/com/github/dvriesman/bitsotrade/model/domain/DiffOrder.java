package com.github.dvriesman.bitsotrade.model.domain;

import java.math.BigInteger;
import java.util.List;

public class DiffOrder {

    public String type;

    public String book;

    public BigInteger sequence;

    public List<DiffOrderPayload> payload;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public BigInteger getSequence() {
        return sequence;
    }

    public void setSequence(BigInteger sequence) {
        this.sequence = sequence;
    }

    public List<DiffOrderPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<DiffOrderPayload> payload) {
        this.payload = payload;
    }
}
