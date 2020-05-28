package org.apdoer.trade.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * eventbus 配置
 * @author apdoer
 */
@Data
@ConfigurationProperties(prefix = "eventbus")
@Component
public class EventBusThreadPoolProperties {

    private ThreadPoolConfig stopFlatEventBusConfig;
    private ThreadPoolConfig stopProfitEventBusConfig;
    private ThreadPoolConfig stopLossEventBusConfig;
    private ThreadPoolConfig conditionOrderEventBusConfig;
    private ThreadPoolConfig quotEventBusConfig;
    private ThreadPoolConfig posiOpenEventBusConfig;
    private ThreadPoolConfig posiCloseEventBusConfig;



    @Data
    public static class ThreadPoolConfig {
        private int corePoolSize;
        private int maxPoolSize;
        private int initCapacity;
        private int backPressureSize;
        private long keepAlive;
    }
}
