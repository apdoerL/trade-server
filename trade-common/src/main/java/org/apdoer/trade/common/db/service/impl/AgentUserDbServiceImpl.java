package org.apdoer.trade.common.db.service.impl;

import org.apdoer.trade.common.db.mapper.AgentSonMapper;
import org.apdoer.trade.common.db.service.AgentUserDbService;
import org.apdoer.trade.common.model.dto.AgentUserCurrencyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AgentUserDbServiceImpl implements AgentUserDbService {

    @Resource
    private AgentSonMapper agentSonMapper;

    @Autowired
    private AgentUserDbServiceImpl selfService;

    @Override
    public AgentUserCurrencyDto getAgentBySon(Integer sonUserId) {
        Integer agentId = agentSonMapper.getAgentIdBySon(sonUserId);
        if (null == agentId) {
            return null;
        }
        return selfService.getAgentCache(agentId);
    }

    @Cacheable(value = "Trade", key = "'Agent_User_'+#agentUserId.toString()")
    public AgentUserCurrencyDto getAgentCache(Integer agentUserId) {
        return agentSonMapper.getAgentInfo(agentUserId);
    }
}