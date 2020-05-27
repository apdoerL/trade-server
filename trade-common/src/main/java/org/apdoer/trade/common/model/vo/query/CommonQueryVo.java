package org.apdoer.trade.common.model.vo.query;

import lombok.Data;

@Data
public class CommonQueryVo {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}