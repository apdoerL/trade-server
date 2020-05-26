package org.apdoer.trade.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 持仓mq 配置
 * @author apdoer
 */
@Data
@ConfigurationProperties(prefix = "rocket-mq.posi-property")
@Component
public class PosiMqProperties {
    private String accessKey;
    private String secretKey;
    private String namesrvAddr;
    private ClosePosiMqProducerProperties closeProducer;
    private OpenPosiMqProducerProperties openProducer;
    private LendingOpenPosiMqProducerProperties lendingOpenProducer;
    private LendingOpenPosiMqConsumerProperties lendingOpenConsumer;

    @Data
    public static class ClosePosiMqProducerProperties {
        private String groupId;
        private String topic;
        private String tags;
    }

    @Data
    public static class OpenPosiMqProducerProperties {
        private String groupId;
        private String topic;
        private String tags;
    }

    @Data
    public static class LendingOpenPosiMqProducerProperties{
        private String groupId;
        private String topic;
        private String tags;
    }

    @Data
    public static class LendingOpenPosiMqConsumerProperties{
        private String groupId;
        private String topic;
        private String tags;
        private String messageModel;
        private Integer maxReconsumeTimes;
    }
}