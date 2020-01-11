package io.gitee.zhucan123.rocketonsspringbootstarter.producer;

import org.springframework.stereotype.Component;

/**
 * @author: zhuCan
 * @date: 2020/1/10 18:05
 * @description:
 */
@Component
public class ExampleProducer extends DefaultProducerProxy {

  @Override
  public String getTopic() {
    return "example-producer-topic";
  }

  @Override
  public String getGroup() {
    return "example-producer-group";
  }

  @Override
  public Boolean getEnable() {
    return true;
  }
}
