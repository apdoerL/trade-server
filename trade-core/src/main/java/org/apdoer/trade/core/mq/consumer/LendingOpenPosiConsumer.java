package org.apdoer.trade.core.mq.consumer;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 15:55
 */
@Slf4j
public class LendingOpenPosiConsumer {

    private Properties properties;

    private MessageListener listener;

    private Consumer consumer;

    private String topic;

    private String tags;

    public LendingOpenPosiConsumer(Properties properties, MessageListener messageListener, String topic, String tags) {
        this.properties = properties;
        this.listener = messageListener;
        this.topic = topic;
        this.tags = tags;
    }


    public void init() {
        log.info("[lending open posi] mq prepare consumer start. topic={}", topic);
        consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(topic, tags, listener);
        log.info("[lending open posi] mq consumer start success. topic={}", topic);
    }


    public void destroy() {
        log.info("[lending open posi] consumer shutdown start");
        consumer.shutdown();
        log.info("[lending open posi] consumer shutdown end");
    }


}
