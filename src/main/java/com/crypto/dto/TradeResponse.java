package com.crypto.dto;

import com.crypto.model.Trade;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TradeResponse extends ApiResponse<TradeResponse.Result> {

    @Getter
    @Setter
    public static class Result {
        @JsonProperty("instrument_name")
        private String instrument;
        private List<Trade> data;
    }
}
