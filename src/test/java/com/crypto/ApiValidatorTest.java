package com.crypto;

import com.crypto.dto.CandleStickResponse;
import com.crypto.dto.TradeResponse;
import com.crypto.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

class ApiValidatorTest {

    private String stickUrl = "https://api.crypto.com/v2/public/get-candlestick?instrument_name=BTC_USDT&timeframe=1m";
    private String tradeUrl = "https://api.crypto.com/v2/public/get-trades?instrument_name=BTC_USDT";


    private String fetch(String url) throws IOException {
        String responseText;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response1 = httpclient.execute(new HttpGet(url))) {
                HttpEntity entity1 = response1.getEntity();
                responseText = EntityUtils.toString(entity1);
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            }
        }
        return responseText;
    }

    @Test
    void valid() throws IOException {
        String jStickResp = fetch(stickUrl);
        String jTradeResp = fetch(tradeUrl);
        System.out.println("jStickResp="+jStickResp);
        System.out.println("jTradeResp="+jTradeResp);

        CandleStickResponse stickResponse = JsonUtils.parse(jStickResp, CandleStickResponse.class);
        TradeResponse tradeResponse = JsonUtils.parse(jTradeResp, TradeResponse.class);

        ApiValidator validator = new ApiValidator(stickResponse);
        validator.valid(tradeResponse.getResult().getData());
    }
}