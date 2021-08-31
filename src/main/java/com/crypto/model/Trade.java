package com.crypto.model;

import com.crypto.enums.Side;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;



@Setter
@Getter
public class Trade {
    @JsonProperty("i")
    private String instrument;
    @JsonProperty("s")
    private Side side;
    @JsonProperty("p")
    private BigDecimal price;
    @JsonProperty("q")
    private BigDecimal quantity;
    @JsonProperty("d")
    private Long id;
    @JsonProperty("t")
    private Long timestamp;
}
