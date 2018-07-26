package com.github.dvriesman.bitsotrade.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.dvriesman.bitsotrade.model.types.OpTypeEnum;
import com.github.dvriesman.bitsotrade.model.types.StatusEnum;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class JsonUtil {

    public static class UnixDateTimeDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Long theTimeStamp = Long.valueOf(Long.valueOf(p.getText()) / 1000);
            //TODO - CHECK UTC COMPLIANT
            Date myDate = Date.from(Instant.ofEpochSecond(theTimeStamp));
            return myDate;
        }

    }

    public static class StatusDeserializer extends JsonDeserializer<StatusEnum> {
        @Override
        public StatusEnum deserialize(JsonParser p, DeserializationContext dc)  {
            try {
                return StatusEnum.valueOf(p.getText().toUpperCase());
            } catch (Exception e) {
                return StatusEnum.UNKNWOWN;
            }
        }

    }

    public static class OpTypeDeserializer extends JsonDeserializer<OpTypeEnum> {
        @Override
        public OpTypeEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText().equals("0") ? OpTypeEnum.BUY : OpTypeEnum.SELL;
        }
    }

}
