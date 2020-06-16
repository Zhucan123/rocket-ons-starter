package io.gitee.zhucan123.ons.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import io.gitee.zhucan123.ons.OnsConfiguration;
import io.gitee.zhucan123.ons.PropertyResolver;
import io.gitee.zhucan123.ons.RocketProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author: zhuCan
 * @date: 2019/11/6 18:46
 * @description: 通用的生产者基类
 */

public abstract class DefaultProducerProxy {

  @Autowired
  protected RocketProperties configuration;

  @Autowired
  private PropertyResolver propertyResolver;

  protected Producer producer;

  protected Logger logger = LoggerFactory.getLogger(this.getClass());


  /**
   * 初始化 producer
   */
  @PostConstruct
  public void setup() {
    if (onsConfiguration() == null || !"on".equalsIgnoreCase(onsConfiguration().enable())) {
      logger.warn("配置开关已关闭!");
      return;
    }
    Properties properties = configuration.rocketProperties();
    properties.put(PropertyKeyConst.GROUP_ID, propertyResolver.springElResolver(onsConfiguration().group()));

    producer = ONSFactory.createProducer(properties);
    logger.info("启动 producer :-> {}", properties.get(PropertyKeyConst.GROUP_ID));

    producer.start();
  }

  public <T> SendResult sendMsg(T msg, String tag) {
    printLog(msg);
    return producer.send(new Message(getTopic(), tag, toByte(msg)));
  }

  /**
   * 发送主题消息
   *
   * @param msg
   * @param tag
   * @param <T>
   * @return
   */
  public <T> SendResult sendMsg(T msg, String tag, String key) {
    printLog(msg);
    return producer.send(new Message(getTopic(), tag, key, toByte(msg)));
  }

  /**
   * 发送消息 重载
   *
   * @param msg
   * @param topic
   * @param tag
   * @param key
   * @param <T>
   * @return
   */
  public <T> SendResult sendMsg(T msg, String tag, String key, String topic) {
    printLog(msg);
    return producer.send(new Message(topic, tag, key, toByte(msg)));
  }

  /**
   * 发送 消息
   *
   * @param message
   * @return
   */
  public SendResult sendMsg(Message message) {
    return producer.send(message);
  }

  /**
   * 默认使用基础配置文件中的 topic
   *
   * @return
   */
  public String getTopic() {
    if (onsConfiguration() != null) {
      return propertyResolver.springElResolver(onsConfiguration().topic());
    }
    return configuration.getTopic();
  }

  /**
   * json序列化成字节码
   *
   * @param o
   * @return
   */
  protected byte[] toByte(Object o) {
    return JSON.toJSONString(o).getBytes();
  }

  /**
   * 输出日志
   *
   * @param msg
   * @param <T>
   */
  private <T> void printLog(T msg) {
    logger.info(" {} 生产者发送消息:{} ", getClass().getSimpleName(), msg);
  }

  /**
   * 获取 producer 上面注册的ons配置注解
   *
   * @return
   */
  protected OnsConfiguration onsConfiguration() {
    return this.getClass().getAnnotation(OnsConfiguration.class);
  }

}

