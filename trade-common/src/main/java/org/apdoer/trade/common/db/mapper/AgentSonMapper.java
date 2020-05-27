package org.apdoer.trade.common.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.trade.common.db.model.po.AgentSonPo;
import org.apdoer.trade.common.model.dto.AgentUserCurrencyDto;

public interface AgentSonMapper extends BaseMapper<AgentSonPo> {

    Integer getAgentIdBySon(@Param("sonUserId") Integer sonUserId);

    AgentUserCurrencyDto getAgentInfo(@Param("agentUserId") Integer agentUserId);

    AgentUserCurrencyDto getAgentBySon(@Param("sonUserId") Integer sonUserId);
}