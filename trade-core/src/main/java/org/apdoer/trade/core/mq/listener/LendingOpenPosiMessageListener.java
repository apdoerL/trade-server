package org.apdoer.trade.core.mq.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.enums.CloseTypeEnum;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.core.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * 赠金开仓 listener
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 16:01
 */
@Slf4j
@Service("lendingOpenPosiMessageListener")
public class LendingOpenPosiMessageListener implements MessageListener {
    @Autowired
    private TradeService tradeService;


    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("[lending open posi] received msgId[{}] topic[{}] key[{}]", message.getMsgID(), message.getTopic(), message.getKey());
        if (null == message.getBody() || message.getBody().length == 0) {
            log.error("[lending open posi] message error msgId[{}] topic[{}] key[{}]", message.getMsgID(), message.getTopic(), message.getKey());
            return Action.CommitMessage;
        }
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        CoreContractPosiPo posiPo = JacksonUtil.jsonToObj(body, CoreContractPosiPo.class);

        OrderCloseReqVo reqVo = new OrderCloseReqVo();
        reqVo.setCloseType(CloseTypeEnum.USER_CLOSE.getCode());
        reqVo.setContractId(posiPo.getContractId());
        reqVo.setUserId(posiPo.getUserId());
        reqVo.setUuid(posiPo.getUuid());
        reqVo.setUserPrice(BigDecimal.ZERO);

        //赠金平仓
        ResultVo resultVo = tradeService.closeOrder(reqVo);
        if (resultVo.getCode() == ExceptionCode.POSI_NOT_EXIST.getCode()) {
            //如果持仓不存在,则继续消费下一条
            return Action.CommitMessage;
        } else if (ExceptionCode.SUCCESS.getCode() == resultVo.getCode()) {
            return Action.CommitMessage;
        } else {
            //重新消费
            return Action.ReconsumeLater;
        }
    }
}
