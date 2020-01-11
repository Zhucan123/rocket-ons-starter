package io.gitee.zhucan123.rocketonsspringbootstarter.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import io.gitee.zhucan123.rocketonsspringbootstarter.MessageData;
import org.springframework.stereotype.Component;

/**
 * @author: zhuCan
 * @date: 2020/1/10 18:03
 * @description:
 */
@Component
@ConsumerListener(tags = "msg_tag", consumers = 2)
public class ExampleConsumerListener implements RocketListener<MessageData> {

  @Override
  public String getTopic() {
    return "topic-example";
  }

  @Override
  public String getGroup() {
    return "group-example";
  }

  @Override
  public Boolean getEnable() {
    return true;
  }

  @Override
  public Action consume(Message message, MessageData messageBody, ConsumeContext consumeContext) {

    // do someThing!

    return Action.CommitMessage;
  }
}
