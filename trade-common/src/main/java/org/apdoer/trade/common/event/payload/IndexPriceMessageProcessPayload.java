package org.apdoer.trade.common.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndexPriceMessageProcessPayload {

    //内部数据传输通道
    private String systemChannel;
    //数据
    private Object data;
}
