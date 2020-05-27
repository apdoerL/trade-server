package org.apdoer.trade.common.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 代理人币种
 * @author apdoer
 */
@Data
public class AgentUserCurrencyDto {
    private Integer agentUserId;
    private List<AgentUserCurrencyWrapDto> currencyList;
}
