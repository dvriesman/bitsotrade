package com.github.dvriesman.bitsotrade.model.domain;

import java.util.Objects;

public class BookEntity {

    private String oid;
    private String book;
    private Double price;
    private Double amount;

    public BookEntity() {

    }

    public BookEntity(String oid) {
        this.oid = oid;
    }

    public BookEntity(String oid, String book, Double price, Double amount) {
        this.oid = oid;
        this.book = book;
        this.price = price;
        this.amount = amount;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%1s -  %2$,.8f  |  %3$,.2f", oid, amount, price);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(oid, that.oid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid);
    }
}
