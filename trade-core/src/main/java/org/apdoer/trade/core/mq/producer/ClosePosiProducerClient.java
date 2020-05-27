package org.apdoer.trade.core.mq.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.apdoer.common.service.util.JacksonUtil;
import org.apdoer.trade.core.config.PosiRocketMqConfig;
import org.apdoer.trade.common.properties.PosiMqProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 平仓mq producer
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 16:25
 */
@Component
@ConditionalOnBean(PosiRocketMqConfig.class)
public class ClosePosiProducerClient {


    @Autowired
    @Qualifier("closePosiProducer")
    private Producer producer;

    @Autowired
    private PosiMqProperties properties;

    public SendResult sendMsg(String key, Object data) {
        return this.sendMsg(
                properties.getCloseProducer().getTopic(),
                properties.getCloseProducer().getTags(),
                key,
                JacksonUtil.toJson(data)
        );
    }

    public SendResult sendMsg(String topic, String tags, String body) {
        return this.sendMsg(topic, tags, "", body);
    }

    public SendResult sendMsg(String topic, String tags, String key, String body) {
        Message msg = new Message(topic, tags, key, body.getBytes(StandardCharsets.UTF_8));
        return this.doSend(msg);
    }

    private SendResult doSend(Message msg) {
        return producer.send(msg);
    }

}
