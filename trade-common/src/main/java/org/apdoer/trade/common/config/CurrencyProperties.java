package org.apdoer.trade.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrencyProperties {

    // 赠金币种
    public static int LENDING_CURRENCY_ID;

    @Value("${trade-server.currency-id.lending:8}")
    public void setLendingCurrencyId(int lendingCurrencyId) {
        LENDING_CURRENCY_ID = lendingCurrencyId;
    }
}