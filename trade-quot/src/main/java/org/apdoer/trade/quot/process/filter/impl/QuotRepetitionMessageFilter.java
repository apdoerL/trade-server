package org.apdoer.trade.quot.process.filter.impl;


import org.apdoer.trade.common.model.dto.IndexPriceDto;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.filter.Filter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * 行情消息过滤器
 *
 * @author apdoer
 */
public class QuotRepetitionMessageFilter implements Filter<IndexPriceMessageSourcePayload, IndexPriceMessageSourcePayload> {

    private Map<Integer, IndexPriceDto> indexPriceMap = new HashMap<>();

    @Override
    public IndexPriceMessageSourcePayload filter(IndexPriceMessageSourcePayload payload) {
        if (null != payload && null != payload.getData()) {
            IndexPriceDto indexPriceDto = (IndexPriceDto) payload.getData();
            if (this.indexPriceMap.containsKey(indexPriceDto.getContractId())) {
                BigDecimal cacheIndexPrice = this.indexPriceMap.get(indexPriceDto.getContractId()).getIndexPrice();
                //去除兩次一样的指数
                if (indexPriceDto.getIndexPrice().compareTo(cacheIndexPrice) != 0) {
                    this.indexPriceMap.put(indexPriceDto.getContractId(), indexPriceDto);
                    return payload;
                } else {
                    return null;
                }
            } else {
                this.indexPriceMap.put(indexPriceDto.getContractId(), indexPriceDto);
                return payload;
            }
        } else {
            return null;
        }
    }

}
