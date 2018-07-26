package com.github.dvriesman.bitsotrade.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TradesPayload {

    private String book;

    private transient boolean virtual;

    @JsonProperty("created_at")
    private Date createdAt;

    private Double amount;

    @JsonProperty("maker_side")
    private String makerSide;

    private Double price;
    private String tid;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMakerSide() {
        return makerSide;
    }

    public void setMarkerSide(String makerSide) {
        this.makerSide = makerSide;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }


}
