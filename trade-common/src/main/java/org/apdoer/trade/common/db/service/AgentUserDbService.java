package org.apdoer.trade.common.db.service;

import org.apdoer.trade.common.model.dto.AgentUserCurrencyDto;

public interface AgentUserDbService {
    /**
     * 根据son获取agent_user_id，并校验agent是否允许分成
     * @param sonUserId
     * @return
     */
    AgentUserCurrencyDto getAgentBySon(Integer sonUserId);
}
