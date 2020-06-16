package io.gitee.zhucan123.ons;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author: zhuCan
 * @date: 2019/10/30 16:38
 * @description: rocket 基础配置属性
 */
@ConfigurationProperties(prefix = "rocket")
public class RocketProperties {

  private String secretKey = "secretKey";

  private String topic = "topic";

  private String address = "http://localhost:80";

  private String accessKey = "accessKey";

  private boolean enable = true;

  private Long delay = 5000L;

  public Properties rocketProperties() {
    Properties properties = new Properties();
    properties.put(PropertyKeyConst.AccessKey, accessKey);
    properties.put(PropertyKeyConst.SecretKey, secretKey);
    properties.put(PropertyKeyConst.NAMESRV_ADDR, address);
    // 集群消费模式
    properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
    return properties;

  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public boolean getEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public Long getDelay() {
    return delay;
  }

  public void setDelay(Long delay) {
    this.delay = delay;
  }
}
