package org.apdoer.trade.core.config;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.apdoer.trade.common.properties.PosiMqProperties;
import org.apdoer.trade.core.mq.consumer.LendingOpenPosiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 15:44
 */
@Configuration
@ConditionalOnProperty(name = "rocket-mq.posi-property.enabled",havingValue = "true")
public class LendingPosiRocketMqConfig {

    @Autowired
    private PosiMqProperties properties;

    @Autowired
    private MessageListener lendingOpenPosiMessageListener;

    @Bean(name = "lendingOpenPosiProducer",initMethod = "start",destroyMethod = "shutdown")
    public ProducerBean lendingOpenPosiProducer(){
        Properties properties  = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.properties.getLendingOpenProducer().getGroupId());
        properties.setProperty(PropertyKeyConst.AccessKey, this.properties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, this.properties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.properties.getNamesrvAddr());

        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(properties);
        return producerBean;
    }

    @Bean(name = "lendingOpenPosiConsumer",initMethod = "init",destroyMethod = "destroy")
    public LendingOpenPosiConsumer lendingOpenPosiConsumer(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.properties.getLendingOpenConsumer().getGroupId());
        properties.setProperty(PropertyKeyConst.AccessKey, this.properties.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, this.properties.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.properties.getNamesrvAddr());

        //订阅方式(默认集群)
        properties.put(PropertyKeyConst.MessageModel,this.properties.getLendingOpenConsumer().getMessageModel());
        //消费消息最大重试次数
        //最大重试次数小于等于16次,重试的时间间隔通上表述
        //大于16次,超过16次的时间间隔为2小时
        properties.put(PropertyKeyConst.MaxReconsumeTimes,this.properties.getLendingOpenConsumer().getMaxReconsumeTimes());

        return new LendingOpenPosiConsumer(properties,lendingOpenPosiMessageListener,this.properties.getLendingOpenConsumer().getTopic(),this.properties.getLendingOpenConsumer().getTags());
    }
}
