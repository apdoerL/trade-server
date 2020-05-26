package org.apdoer.trade.quot.process.cleaner.impl;


import org.apdoer.trade.quot.enums.QuotMessageTypeEnum;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.cleaner.Cleaner;

/**
 * 行情消息清洗
 *
 * @author apdoer
 */
public class QuotMessageCleaner implements Cleaner<IndexPriceMessageSourcePayload, IndexPriceMessageSourcePayload> {

    @Override
    public IndexPriceMessageSourcePayload clean(IndexPriceMessageSourcePayload data) {
        if (null != data && QuotMessageTypeEnum.INDEX_PRICE.getCode() == data.getMessageType()) {
            return data;
        } else {
            return null;
        }
    }

}
