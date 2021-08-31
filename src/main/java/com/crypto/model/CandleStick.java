package com.crypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class CandleStick {
    @JsonProperty("o")
    private BigDecimal open;
    @JsonProperty("h")
    private BigDecimal high;
    @JsonProperty("l")
    private BigDecimal low;
    @JsonProperty("c")
    private BigDecimal close;
    @JsonProperty("t")
    private Long timestamp;
    private Long openTime;
    private Long closeTime;
    private List<Trade> trades = new LinkedList<>();


    private static Comparator<CandleStick> candleStickComparator = Comparator.comparing(CandleStick::getOpen)
            .thenComparing(CandleStick::getHigh)
            .thenComparing(CandleStick::getLow)
            .thenComparing(CandleStick::getClose);

    public CandleStick() {}
    public CandleStick(Long timestamp) {
        this.timestamp = timestamp;
    }

    public CandleStick add(Trade trade) {
        if(isValid(trade)) {
            return this;
        }

        if (open==null || trade.getTimestamp() < openTime) {
            open = trade.getPrice();
            openTime = trade.getTimestamp();
        }
        if (high==null || trade.getPrice().compareTo(high)==1) {
            high = trade.getPrice();
        }
        if (low==null || trade.getPrice().compareTo(low)==-1) {
            low = trade.getPrice();
        }
        if (close==null || trade.getTimestamp() > closeTime ) {
            close = trade.getPrice();
            closeTime = trade.getTimestamp();
        }
        trades.add(trade);
        return this;
    }

    private boolean isValid(Trade trade) {
        return trade.getPrice()==null || trade.getPrice().signum()<=0 || trade.getTimestamp()==null;
    }

    public boolean isTheSame(CandleStick another) {
        return candleStickComparator.compare(this, another)==0;
    }
}
