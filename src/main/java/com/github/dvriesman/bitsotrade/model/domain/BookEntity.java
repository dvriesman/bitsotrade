package com.github.dvriesman.bitsotrade.model.domain;

public class BookEntity {

    private String book;
    private Double price;
    private Double amount;

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
        return String.format("%1$,.8f  |  %2$,.2f", amount, price);

    }
}
