package com.crypto.dto;

import com.crypto.enums.Interval;
import com.crypto.model.CandleStick;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CandleStickResponse extends ApiResponse<CandleStickResponse.Result> {

    @Getter
    @Setter
    public static class Result {
        @JsonProperty("instrument_name")
        private String instrument;
        private String interval;
        private int depth;
        private List<CandleStick> data;
    }
}
