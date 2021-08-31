package com.crypto.model;

import com.crypto.enums.Interval;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CandleStickHolderTest {


    @Test
    void add_invalid() {
        CandleStickHolder stickHolder = new CandleStickHolder("BTC_USDT", Interval.ONE_MINUTE);
        stickHolder.add(new Trade());
        assertThat(stickHolder.getSticks()).isEmpty();

        Trade trade = new Trade();
        trade.setPrice(new BigDecimal("111"));
        stickHolder.add(trade);
        assertThat(stickHolder.getSticks()).isEmpty();

        trade = new Trade();
        trade.setTimestamp(1630332504111L);
        stickHolder.add(trade);
        assertThat(stickHolder.getSticks()).isEmpty();

        trade = new Trade();
        trade.setPrice(new BigDecimal("111"));
        trade.setTimestamp(1630332504111L);
        stickHolder.add(trade);
        assertThat(stickHolder.getSticks()).isNotEmpty();
    }

    @Test
    void add_same_window() {
        CandleStickHolder stickHolder = new CandleStickHolder("BTC_USDT", Interval.ONE_MINUTE);

        stickHolder.add(createTrade(1630332504111L, "100")); //  14:08:24.111
        CandleStick stick = stickHolder.getStick(1630332504111L);
        assertThat(stick).isNotNull();
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // 14:08:00.000

        stickHolder.add(createTrade(1630332500000L, "90")); //  14:08:20  test open
        stick = stickHolder.getStick(1630332500000L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // still 14:08:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("90"));  // open change
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("100"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("90"));  //  lower
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("100"));

        stickHolder.add(createTrade(1630332481000L, "20")); //  14:08:01  test open again
        stick = stickHolder.getStick(1630332481000L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // still  14:08:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("20"));  // open change
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("100"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("20"));   // lower
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("100"));

        stickHolder.add(createTrade(1630332539999L, "200")); //  14:08:59.999
        stick = stickHolder.getStick(1630332539999L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // still  14:08:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("20"));  // open change
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("200"));  // high change
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("20"));
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("200"));

        stickHolder.add(createTrade(1630332480000L, "70")); //  14:08:00.000
        stick = stickHolder.getStick(1630332480000L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // still  14:08:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("70"));  // open change
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("200"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("20"));
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("200"));
    }

    @Test
    void add_different_window() {
        CandleStick stick;
        CandleStickHolder stickHolder = new CandleStickHolder("BTC_USDT", Interval.ONE_MINUTE);

        stickHolder.add(createTrade(1630332539999L, "200")); //  14:08:59.999
        stick = stickHolder.getStick(1630332539999L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // still  14:08:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("200"));
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("200"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("200"));
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("200"));

        stickHolder.add(createTrade(1630332480000L, "70")); //  14:08:00.000
        stick = stickHolder.getStick(1630332480000L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332480000L); // still  14:08:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("70"));
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("200"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("70"));
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("200"));

        // before 14:08:00 window
        stickHolder.add(createTrade(1630332479999L, "7")); //  14:07:59.999
        stick = stickHolder.getStick(1630332479999L);
        assertThat(stick.getTimestamp()).isEqualTo(1630332420000L); //   14:07:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("7"));
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("7"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("7"));
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("7"));
        CandleStick stick140800 = stickHolder.getStick(1630332480000L); // not effect stick140800
        assertThat(stick140800.getOpen()).isEqualTo(new BigDecimal("70"));
        assertThat(stick140800.getHigh()).isEqualTo(new BigDecimal("200"));
        assertThat(stick140800.getLow()).isEqualTo(new BigDecimal("70"));
        assertThat(stick140800.getClose()).isEqualTo(new BigDecimal("200"));

        // after 14:08:00 window
        stickHolder.add(createTrade(1630332539999L+1L, "9")); //  14:09:00.000
        stick = stickHolder.getStick(1630332539999L+1);
        assertThat(stick.getTimestamp()).isEqualTo(1630332539999L+1); //   14:09:00.000
        assertThat(stick.getOpen()).isEqualTo(new BigDecimal("9"));
        assertThat(stick.getHigh()).isEqualTo(new BigDecimal("9"));
        assertThat(stick.getLow()).isEqualTo(new BigDecimal("9"));
        assertThat(stick.getClose()).isEqualTo(new BigDecimal("9"));
        stick140800 = stickHolder.getStick(1630332480000L);   // not effect stick140800
        assertThat(stick140800.getOpen()).isEqualTo(new BigDecimal("70"));
        assertThat(stick140800.getHigh()).isEqualTo(new BigDecimal("200"));
        assertThat(stick140800.getLow()).isEqualTo(new BigDecimal("70"));
        assertThat(stick140800.getClose()).isEqualTo(new BigDecimal("200"));

    }

    @Test
    public void test_5min() {
        CandleStickHolder stickHolder = new CandleStickHolder("BTC_USDT", Interval.FIVE_MINUTE);

        stickHolder.add(createTrade(1630332504111L, "100")); //  14:08:24.111
        CandleStick stick = stickHolder.getStick(1630332504111L);
        assertThat(stick).isNotNull();
        assertThat(stick.getTimestamp()).isEqualTo(1630332300000L); // 14:05:00.000

        stickHolder.add(createTrade(1630332599999L, "100")); //  14:09:59.999
        stick = stickHolder.getStick(1630332599999L);
        assertThat(stick).isNotNull();
        assertThat(stick.getTimestamp()).isEqualTo(1630332300000L); // 14:05:00.000

        stickHolder.add(createTrade(1630332599999L+1L, "100")); //  14:10:00.000
        stick = stickHolder.getStick(1630332599999L+1L);
        assertThat(stick).isNotNull();
        assertThat(stick.getTimestamp()).isEqualTo(1630332600000L); // 14:10:00.000

        stickHolder.add(createTrade(1630332300000L, "100")); //  14:05:00.000
        stick = stickHolder.getStick(1630332300000L);
        assertThat(stick).isNotNull();
        assertThat(stick.getTimestamp()).isEqualTo(1630332300000L); // 14:05:00.000

        stickHolder.add(createTrade(1630332300000L-1L, "100")); //  14:04:59.999
        stick = stickHolder.getStick(1630332300000L-1L);
        assertThat(stick).isNotNull();
        assertThat(stick.getTimestamp()).isEqualTo(1630332000000L); // 14:00:00.000
    }

    private Trade createTrade(long t, String p) {
        Trade trade = new Trade();
        trade.setPrice(new BigDecimal(p));
        trade.setTimestamp(t);
        return trade;
    }


}