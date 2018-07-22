package com.github.dvriesman.bitsotrade.components;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import com.github.dvriesman.bitsotrade.model.types.StatusEnum;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

public class JsonUtil {

    public static class UnixDateTimeDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Long theTimeStamp = Long.valueOf(Long.valueOf(p.getText()) / 1000);
            //TODO - CHECK UTC COMPLIANT
            Date myDate = Date.from(Instant.ofEpochSecond(theTimeStamp));
            return myDate;
        }

    }

    public static class StatusDeserializer extends JsonDeserializer<StatusEnum> {

        @Override
        public StatusEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            try {
                return StatusEnum.valueOf(p.getText().toUpperCase());
            } catch (IllegalArgumentException e) {
                return StatusEnum.UNKNWOWN;
            }
        }

    }

    public static class OpTypeDeserializer extends JsonDeserializer<OpTypeEnum> {

        @Override
        public OpTypeEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return p.getText().equals("1") ? OpTypeEnum.SELL : OpTypeEnum.BUY;
        }
    }

}
