package com.crypto.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CandleStickTest {

    @Test
    void add_no_price() {
        CandleStick stick = new CandleStick(0L);
        Trade trade = new Trade();
        stick.add(trade);
        assertThat(stick.getOpen()).isNull();
        assertThat(stick.getHigh()).isNull();
        assertThat(stick.getLow()).isNull();
        assertThat(stick.getClose()).isNull();
    }

    @Test
    void add_price_first() {
        CandleStick stick = new CandleStick(0L);
        stick.add(createTrade("10", 1L));
        assertThat(stick.getOpen()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(stick.getClose()).isEqualByComparingTo(new BigDecimal("10"));
    }

    @Test
    void add_price_test_open_close() {
        CandleStick stick = new CandleStick(0L);
        for (long i = 1; i <= 100; i++) {
            Trade trade = new Trade();
            trade.setPrice(BigDecimal.valueOf(i));
            trade.setTimestamp(100L+i); // t = 100~200
            stick.add(trade);
        }
        assertThat(stick.getOpen()).isEqualByComparingTo(new BigDecimal("1"));
        assertThat(stick.getClose()).isEqualByComparingTo(new BigDecimal("100"));

        stick.add(createTrade("555", 150L));
        assertThat(stick.getOpen()).isEqualByComparingTo(new BigDecimal("1"));
        assertThat(stick.getClose()).isEqualByComparingTo(new BigDecimal("100"));

        stick.add(createTrade("666", 260L));
        assertThat(stick.getOpen()).isEqualByComparingTo(new BigDecimal("1"));
        assertThat(stick.getClose()).isEqualByComparingTo(new BigDecimal("666"));

        stick.add(createTrade("0.99", 80L));
        assertThat(stick.getOpen()).isEqualByComparingTo(new BigDecimal("0.99"));
        assertThat(stick.getClose()).isEqualByComparingTo(new BigDecimal("666"));
    }

    @Test
    void add_price_test_high() {
        CandleStick stick = new CandleStick(0L);
        stick.add(createTrade("10", 1L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("10"));

        stick.add(createTrade("10.00", 2L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("10"));

        stick.add(createTrade("10.0001", 3L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("10.0001"));

        stick.add(createTrade("10", 4L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("10.0001"));

        stick.add(createTrade("11", 5L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("11"));

        stick.add(createTrade("9", 6L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("11"));

        stick.add(createTrade("0", 7L));
        assertThat(stick.getHigh()).isEqualByComparingTo(new BigDecimal("11"));
    }


    @Test
    void add_price_test_low() {
        CandleStick stick = new CandleStick(0L);
        stick.add(createTrade("1000", 1L));
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("1000"));

        stick.add(createTrade("1000.0001", 2L));
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("1000"));

        stick.add(createTrade("999.99999999", 3L));
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("999.99999999"));

        stick.add(createTrade("10", 4L));
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("10"));

        stick.add(createTrade("0", 5L)); // no effect
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("10"));

        stick.add(createTrade("-1", 6L)); // no effect
        assertThat(stick.getLow()).isEqualByComparingTo(new BigDecimal("10"));

    }
    private Trade createTrade(String price, long time) {
        Trade trade = new Trade();
        trade.setPrice(new BigDecimal(price));
        trade.setTimestamp(time);
        return trade;
    }


}