package com.crypto.enums;

import lombok.Getter;

@Getter
public enum Interval {
    ONE_MINUTE("1m", 60*1000),
    FIVE_MINUTE("5m", 5 * 60*1000),
    ;

    private final String code;
    private final long interval; //ms

    Interval(String code, long interval) {
        this.code = code;
        this.interval = interval;
    }

    public static Interval of(String code) {
        for (Interval interval : values()) {
            if (interval.getCode().equals(code)) {
                return interval;
            }
        }
        return null;
    }
}
