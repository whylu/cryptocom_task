package com.crypto;

import com.crypto.dto.CandleStickResponse;
import com.crypto.enums.Interval;
import com.crypto.model.CandleStick;
import com.crypto.model.CandleStickHolder;
import com.crypto.model.Trade;
import com.crypto.utils.JsonUtils;

import java.util.List;

public class ApiValidator {
    private CandleStickHolder stickHolder;
    private List<CandleStick> goodSticks;

    public ApiValidator(CandleStickResponse response) {
        CandleStickResponse.Result result = response.getResult();
        stickHolder = new CandleStickHolder(result.getInstrument(), Interval.of(result.getInterval()));
        goodSticks = result.getData();
    }

    public boolean valid(List<Trade> trades) {
        boolean isPass = true;
        for (Trade trade : trades) {
            stickHolder.add(trade);
        }
        for (CandleStick goodStick : goodSticks) {
            CandleStick stick = stickHolder.getStick(goodStick.getTimestamp());
            if (stick==null) {
                continue;
            }
            if (!goodStick.isTheSame(stick)) {
                System.out.println("expecting " + JsonUtils.toString(goodStick) + " but "+JsonUtils.toString(stick));;
                isPass = false;
            } else {
                System.out.println("good stick: "+ stick.getTimestamp());
            }
        }
        return isPass;
    }

}
