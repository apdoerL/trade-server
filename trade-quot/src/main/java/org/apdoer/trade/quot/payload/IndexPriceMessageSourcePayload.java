package org.apdoer.trade.quot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexPriceMessageSourcePayload {
    private Integer messageType;
    private Object data;
}
