package io.gitee.zhucan123.ons.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import io.gitee.zhucan123.ons.MessageData;
import io.gitee.zhucan123.ons.RocketConfiguration;
import io.gitee.zhucan123.ons.TopicManager;
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

public abstract class DefaultProducerProxy implements TopicManager {

  @Autowired
  protected RocketConfiguration configuration;

  protected Producer producer;

  protected Logger logger = LoggerFactory.getLogger(this.getClass());


  /**
   * 初始化 producer
   */
  @PostConstruct
  public void setup() {
    if (!configuration.getEnable()) {
      logger.warn("配置开关已关闭!");
      return;
    }
    Properties properties = configuration.rocketProperties();
    properties.put(PropertyKeyConst.GROUP_ID, getGroup());

    producer = ONSFactory.createProducer(properties);
    logger.info("启动 producer :-> {}", properties.get(PropertyKeyConst.GROUP_ID));

    producer.start();
  }

  public <T extends MessageData> SendResult sendMsg(T msg, String tag) {
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
  public <T extends MessageData> SendResult sendMsg(T msg, String tag, String key) {
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
  public <T extends MessageData> SendResult sendMsg(T msg, String tag, String key, String topic) {
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


  protected byte[] toByte(Object o) {
    return JSONObject.toJSONString(o).getBytes();
  }

  private String singleTagString(String... tags) {
    return String.join("||", tags);
  }

  private <T extends MessageData> void printLog(T msg) {
    logger.info(getClass().getSimpleName() + " 生产者发送消息: " + msg.toString());
  }

}

