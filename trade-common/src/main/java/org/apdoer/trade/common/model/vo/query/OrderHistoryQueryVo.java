package org.apdoer.trade.common.model.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderHistoryQueryVo extends CommonQueryVo {
    private Integer userId;
    // 查询日期
    private Long queryDate;
}