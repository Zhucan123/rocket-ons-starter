package io.gitee.zhucan123.ons.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.shade.com.alibaba.fastjson.parser.Feature;
import io.gitee.zhucan123.ons.MessageData;
import io.gitee.zhucan123.ons.TopicManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: zhuCan
 * @date: 2019/11/13 11:28
 * @description: 注册consumer 的基类
 */
public interface RocketListener<T extends MessageData> extends TopicManager, MessageListener {

  @Override
  default Action consume(Message message, ConsumeContext context) {
    return consume(message, JSONObject.parseObject(message.getBody(), getJsonType().getType(), Feature.SupportAutoType), context);
  }

  Action consume(Message message, T messageBody, ConsumeContext consumeContext);

  default TypeReference<T> getJsonType() {
    Type[] actualTypeArguments = ((ParameterizedType) (getClass().getGenericSuperclass())).getActualTypeArguments();
    if (actualTypeArguments != null && actualTypeArguments.length > 0) {
      return new TypeReference<T>() {
        @Override
        public Type getType() {
          return actualTypeArguments[0];
        }
      };
    }
    return null;
  }
}
