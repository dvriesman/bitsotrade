package com.github.dvriesman.bitsotrade.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.dvriesman.bitsotrade.components.JsonUtil;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import com.github.dvriesman.bitsotrade.model.types.StatusEnum;

import java.math.BigDecimal;
import java.util.Date;

public class DiffOrderPayload {

    @JsonProperty("o")
    private String id;

    @JsonProperty("d")
    @JsonDeserialize(using=JsonUtil.UnixDateTimeDeserializer.class)
    private Date date;

    @JsonProperty("t")
    @JsonDeserialize(using=JsonUtil.OpTypeDeserializer.class)
    private OpTypeEnum type;

    @JsonProperty("r")
    private BigDecimal rate;

    @JsonProperty("a")
    private BigDecimal amount;

    @JsonProperty("v")
    private BigDecimal value;

    @JsonProperty("s")
    @JsonDeserialize(using=JsonUtil.StatusDeserializer.class)
    private StatusEnum status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OpTypeEnum getType() {
        return type;
    }

    public void setType(OpTypeEnum type) {
        this.type = type;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
