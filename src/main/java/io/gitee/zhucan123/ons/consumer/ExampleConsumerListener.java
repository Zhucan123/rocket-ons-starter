package io.gitee.zhucan123.ons.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import io.gitee.zhucan123.ons.MessageData;
import io.gitee.zhucan123.ons.OnsConfiguration;


/**
 * @author: zhuCan
 * @date: 2020/1/10 18:03
 * @description:
 */

/**
 * @Component 注册成一个bean
 * @ConsumerListener 标识是一个ons的消息消费者监听器
 * 1. tags:接受带有相关tag的消息
 * 2. consumers启动的实例数量
 * <p>
 * MessageData 为消息体类型class
 * consume 为消费业务逻辑
 */
@ConsumerListener(tags = "msg_tag", consumers = 2)
@OnsConfiguration(topic = "topic-example", group = "group-example")
public class ExampleConsumerListener implements RocketListener<MessageData> {

  @Override
  public Action consume(Message message, MessageData messageBody, ConsumeContext consumeContext) {

    // do someThing!

    return Action.CommitMessage;
  }
}
