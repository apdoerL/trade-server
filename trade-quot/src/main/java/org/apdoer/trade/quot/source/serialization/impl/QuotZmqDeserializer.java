package org.apdoer.trade.quot.source.serialization.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.source.serialization.ZmqDeserializer;

/**
 *  外部行情数据接入,反序列化器
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:01
 */
@Slf4j
public class QuotZmqDeserializer implements ZmqDeserializer {

    private static final long WARN_REALTIME_INTER = 60000000;

    @Override
    public IndexPriceMessageSourcePayload deserialize(String data) {
        if (null != data) {
            return this.buildMessageSourcePayload(data);
        } else {
            return null;
        }
    }

    private IndexPriceMessageSourcePayload buildMessageSourcePayload(String data) {
        log.debug("QUOT origin data[{}]", data);
        IndexPriceDto indexPriceDto = JacksonUtil.jsonToObj(data, IndexPriceDto.class);

        if (System.currentTimeMillis() - indexPriceDto.getTime() > WARN_REALTIME_INTER) {
            log.error("index price later, data={}", data);
        }

        IndexPriceMessageSourcePayload payload = new IndexPriceMessageSourcePayload();
        payload.setMessageType(indexPriceDto.getMessageType());
        payload.setData(indexPriceDto);
        return payload;
    }
}
