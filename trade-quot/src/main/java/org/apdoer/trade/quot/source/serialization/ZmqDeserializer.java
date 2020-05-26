package org.apdoer.trade.quot.source.serialization;

import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 10:58
 */
public interface ZmqDeserializer extends Deserializer<String,IndexPriceMessageSourcePayload > {
}
