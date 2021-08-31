package com.crypto;

import com.crypto.dto.CandleStickResponse;
import com.crypto.dto.TradeResponse;
import com.crypto.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class App {
    private static String stickUrl = "https://api.crypto.com/v2/public/get-candlestick?instrument_name=BTC_USDT&timeframe=1m";
    private static String tradeUrl = "https://api.crypto.com/v2/public/get-trades?instrument_name=BTC_USDT";

    public static void main(String[] args) throws IOException {
        String instrument;
        if (args.length==1) {
            instrument = args[0];
            stickUrl = stickUrl.replace("BTC_USDT", instrument);
            tradeUrl = tradeUrl.replace("BTC_USDT", instrument);
        }
        verify();
    }

    private static String fetch(String url) throws IOException {
        String responseText;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response1 = httpclient.execute(new HttpGet(url))) {
                HttpEntity entity1 = response1.getEntity();
                responseText = EntityUtils.toString(entity1);
                EntityUtils.consume(entity1);
            }
        }
        return responseText;
    }

    private static void verify() throws IOException {
        System.out.println("fetch data from API --");
        String jStickResp = fetch(stickUrl);
        String jTradeResp = fetch(tradeUrl);
        System.out.println("jStickResp="+jStickResp);
        System.out.println("jTradeResp="+jTradeResp);

        CandleStickResponse stickResponse = JsonUtils.parse(jStickResp, CandleStickResponse.class);
        TradeResponse tradeResponse = JsonUtils.parse(jTradeResp, TradeResponse.class);

        System.out.println("start verify --");
        ApiValidator validator = new ApiValidator(stickResponse);
        boolean isPass = validator.valid(tradeResponse.getResult().getData());
        System.out.println(isPass? "pass" : "not pass");
    }
}
