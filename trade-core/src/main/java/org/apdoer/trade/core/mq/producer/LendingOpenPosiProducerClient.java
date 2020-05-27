package org.apdoer.trade.core.mq.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.trade.core.config.PosiRocketMqConfig;
import org.apdoer.trade.common.properties.PosiMqProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 赠金开仓,延迟消息mq client
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 15:39
 */
@Service
@ConditionalOnBean(PosiRocketMqConfig.class)
public class LendingOpenPosiProducerClient {

    @Autowired
    private PosiMqProperties properties;

    @Autowired
    @Qualifier("lendingOpenPosiProducer")
    private Producer producer;

    // 赠金开仓后，自动平仓的间隔，单位：小时
    @Value("${trade-server.posi.lending-delay:24}")
    public int lendingDelay;

    private static final long DELAY_TIME = 60 * 60 * 1000;

    public SendResult sendMsg(String key, Object data) {
        return this.sendMsg(
                properties.getLendingOpenProducer().getTopic(),
                properties.getLendingOpenProducer().getTags(),
                key,
                JacksonUtil.toJson(data)
        );
    }

    public SendResult sendMsg(String topic, String tags, String body) {
        return this.sendMsg(topic, tags, "", body);
    }

    public SendResult sendMsg(String topic, String tags, String key, String body) {
        Message msg = new Message(topic, tags, key, body.getBytes(StandardCharsets.UTF_8));
        msg.setStartDeliverTime(System.currentTimeMillis() + (lendingDelay * DELAY_TIME));
        return this.doSend(msg);
    }

    private SendResult doSend(Message message) {
        return producer.send(message);
    }
}
