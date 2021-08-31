package com.crypto.model;

import com.crypto.enums.Interval;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class CandleStickHolder {
    private String instrument;
    private Interval interval;

    /**
     * key: windowTime
     */
    private TreeMap<Long, CandleStick> candleStickMap = new TreeMap<>();

    public CandleStickHolder(String instrument, Interval interval) {
        this.instrument = instrument;
        this.interval = interval;
    }

    public void add(Trade trade) {
        if (trade.getTimestamp()==null || trade.getPrice()==null || trade.getPrice().signum()<=0) {
            return;
        }

        long windowTime = (trade.getTimestamp() / interval.getInterval()) * interval.getInterval();
        Map.Entry<Long, CandleStick> stickEntry = candleStickMap.floorEntry(windowTime);
        if (stickEntry==null) { // first
            CandleStick stick = new CandleStick(windowTime).add(trade);
            candleStickMap.put(windowTime, stick);
        } else { // found exist
            CandleStick stick = stickEntry.getValue();
            if (stick.getTimestamp().equals(windowTime)) {
                stick.add(trade);
            } else { //window time not match, create new one
                stick = new CandleStick(windowTime).add(trade);
                candleStickMap.put(windowTime, stick);
            }
        }
    }

    public CandleStick getStick(long timestamp) {
        Map.Entry<Long, CandleStick> stickEntry = candleStickMap.floorEntry(timestamp);
        return stickEntry==null ? null : stickEntry.getValue();
    }

    public Collection<CandleStick> getSticks() {
        return candleStickMap.values();
    }
}
