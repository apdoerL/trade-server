package org.apdoer.trade.core.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.apdoer.trade.common.properties.PosiMqProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 持仓mq配置
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 15:50
 */
@Configuration
@ConditionalOnProperty(name = "rocket-mq.posi-property.enabled",havingValue = "true")
public class PosiRocketMqConfig {

    @Autowired
    private PosiMqProperties properties;


    @Bean(name = "closePosiProducer",initMethod = "start",destroyMethod = "shutdown")
    public ProducerBean closePosiProducer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.properties.getCloseProducer().getGroupId());
        properties.setProperty(PropertyKeyConst.AccessKey, this.properties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, this.properties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.properties.getNamesrvAddr());

        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(properties);
        return producerBean;
    }

    @Bean(name = "openPosiProducer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean openPosiProducer() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.properties.getOpenProducer().getGroupId());
        properties.setProperty(PropertyKeyConst.AccessKey, this.properties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, this.properties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.properties.getNamesrvAddr());

        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(properties);
        return producerBean;
    }
}
