# rocketONS-starter
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](LICENSE)
[![Gitee Stars](https://gitee.com/zc_oss/rocketONS-starter/badge/star.svg?theme=dark)](https://gitee.com/zc_oss/rocketONS-starter)
[![Gitee fork](https://gitee.com/zc_oss/rocketONS-starter/badge/fork.svg?theme=dark)](https://gitee.com/zc_oss/rocketONS-starter)

## 介绍

rocketONS-starter 是一个基于阿里云ONS消息服务的轻量级Spring Boot Starter。它为您提供了一个简单高效的封装，使您能够快速创建RocketMQ生产者和消费者，实现应用之间的消息传递。适用于高并发、高吞吐、低延迟的分布式消息场景。

## 软件架构

rocketONS-starter 基于Spring的ApplicationContext容器管理，自动扫描consumer监听器，并注册启动消费者，用于接收和处理来自阿里云ONS服务分发的消息。根据配置文件动态创建消费者和生产者，自定义消费者的启停开关，并自动序列化和解析消息实体。同时，它还支持消息过滤、顺序消息和延时消息等高级功能，满足不同场景的需求。

## 主要特点

-   简单易用：提供简洁的配置和API，快速上手，轻松实现消息生产和消费。
-   高性能：基于阿里云ONS消息服务，享受高并发、高吞吐、低延迟的分布式消息服务。
-   弹性扩展：根据业务需求自由添加消费者和生产者，实现弹性扩展。
-   高级功能支持：支持消息过滤、顺序消息和延时消息等高级功能，满足不同场景的需求。

## 安装

将以下依赖添加到您的项目中：

```

<dependency>
    <groupId>io.gitee.zhucan123</groupId>
    <artifactId>rocket-ons-spring-boot-starter</artifactId>
    <version>1.0.8</version>
</dependency>
```

## 使用说明

### 1. 将配置添加到项目中

```
yamlCopy code
rocket:
  address: http://xxxx
  secretKey: xxxx
  accessKey: xxxx
  topic: xxxx
  groupSuffix: GID_
  enable: true
  delay: 1000
```

| 参数名         | 类型      | 是否必填 | 默认值   | 描述                                    |
| ----------- | ------- | ---- | ----- | ------------------------------------- |
| accessKey   | String  | 是    | -    | 用于身份认证的AccessKeyId，创建于阿里云账号管理控制台。     |
| secretKey   | String  | 是    | -    | 用于身份认证的AccessKeySecret，创建于阿里云账号管理控制台。 |
| address     | String  | 是    | -    | 设置TCP协议接入点。                           |
| groupSuffix | String  | 否    | GID_ | 控制台创建的Group ID的前缀，通常以"GID_"开头。       |
| topic       | String  | 是    | -    | 当生产者未指定topic时使用的默认绑定topic。            |
| delay       | Integer | 否    | 1000  | 消息发送延迟毫秒数。                            |
| enable      | Boolean | 否    | true  | 是否启用starter。                          |




### 2. 在主程序中启用rocketONS-starter

```

@EnableRocketONS
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

### 3. 示例代码：使用consumer

```

@ConsumerListener(tags = "msg_tag", consumers = 2)
@OnsConfiguration(topic = "topic-example", group = "GID_${example.group}")
public class ExampleConsumerListener implements RocketListener<MessageData> {

  @Override
  public Action consume(Message message, MessageData messageBody, ConsumeContext consumeContext) {

    // 处理业务逻辑

    return Action.CommitMessage;
  }
}
```

-   @OnsConfiguration：注册成一个Spring容器，并设置消费者绑定的topic和group。可设置固定值，也可使用${propertiesKey}的方式读取配置文件中的配置。
-   @ConsumerListener：标识这是一个ONS消息消费者监听器。可以设置tags来进行消息过滤，设置consumers来指定消费者线程数。

### 4. 示例代码：使用producer

```

@Service
public class ExampleProducerService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(MessageData messageData) {
        rocketMQTemplate.syncSend("topic-example:msg_tag", messageData);
    }
}
```

通过注入RocketMQTemplate，使用syncSend方法同步发送消息。方法参数中的字符串格式为topic:tag，表示发送到指定topic并设置消息tag。

## 高级功能

### 1. 顺序消息

使用RocketMQTemplate的syncSendOrderly方法发送顺序消息，确保消费者按照发送顺序进行消息处理。

```

rocketMQTemplate.syncSendOrderly("topic-example:msg_tag", messageData, orderId);
```

### 2. 延时消息

在发送消息时，通过设置messageDelayLevel参数指定延时级别。

```

rocketMQTemplate.syncSend("topic-example:msg_tag", messageData, messageDelayLevel);
```

### 3. 消息过滤

使用@ConsumerListener注解的tags属性来实现消息过滤。设置对应的tag值，消费者将只会消费带有该tag的消息。

```

@ConsumerListener(tags = "msg_tag", consumers = 2)
public class ExampleConsumerListener implements RocketListener<MessageData> { ... }
```

请注意，您需要在生产消息时设置相应的tag。

```

rocketMQTemplate.syncSend("topic-example:msg_tag", messageData); 
```

### 4. 异步发送消息

除了同步发送消息，RocketMQTemplate还提供了异步发送消息的方法。使用asyncSend方法发送消息，提供一个回调函数来处理发送结果。
```
rocketMQTemplate.asyncSend("topic-example:msg_tag", messageData, new SendCallback() {
  @Override 
  public void onSuccess(SendResult sendResult) { 
    // 处理发送成功的逻辑
  }
  @Override 
  public void onException(Throwable e) {
    // 处理发送失败的逻辑 
  } 
});
```
### 5. 广播模式

广播模式允许您将消息发送给所有消费者。要启用广播模式，需要在消费者监听器中设置broadcast属性为true。

```
javaCopy code
@ConsumerListener(tags = "msg_tag", consumers = 2, broadcast = true)
public class ExampleConsumerListener implements RocketListener<MessageData> { ... }
```

### 6. 消息重试策略

当消费者处理消息失败时，可以通过返回Action.ReconsumeLater来触发消息重试。您可以在配置文件中设置重试策略。

```
yamlCopy code
rocket:
  retry: 3
  delay: 1000
```

| 参数名   | 类型      | 是否必填 | 默认值  | 描述         |
| ----- | ------- | ---- | ---- | ---------- |
| retry | Integer | 否    | 3    | 消息重试次数。    |
| delay | Integer | 否    | 1000 | 消息发送延迟毫秒数。 |

### 7. 自定义序列化与反序列化

默认情况下，rocketONS-starter 使用 Java 序列化和反序列化消息。但您可以通过实现MessageSerializer接口来自定义序列化和反序列化策略。 public class CustomMessageSerializer implements MessageSerializer { @Override public byte[] serialize(Object obj) { // 实现自定义的序列化逻辑 } @Override public <T> T deserialize(byte[] data, Class<T> clazz) { // 实现自定义的反序列化逻辑 } }

然后，在配置文件中指定自定义序列化器。

```

rocket:
  serializer: com.example.CustomMessageSerializer
```

## 注意事项

-   确保在生产和消费环境中使用相同的序列化和反序列化策略。
-   当使用Action.ReconsumeLater触发消息重试时，务必注意避免重试风暴，以免影响整体性能。

## 8. 延迟消息

RocketMQ 支持延迟消息发送，可以在发送时指定延迟级别。要发送延迟消息，请使用 sendDelay 方法。

```

int delayLevel = 3; // 延迟级别，具体延迟时间需要参考 RocketMQ 的延迟级别配置
rocketMQTemplate.sendDelay("topic-example:msg_tag", messageData, delayLevel);
```

## 9. 事务消息

RocketMQ 支持发送事务消息，可以在发送消息时关联一个本地事务。使用 sendMessageInTransaction 方法发送事务消息。

```

rocketMQTemplate.sendMessageInTransaction("topic-example:msg_tag", messageData, new LocalTransactionExecuter() {
    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        // 执行本地事务
        // 返回事务状态
    }
}, null);
```

## 10. 集成测试

为了方便集成测试，您可以使用 RocketMQTestListener 注解启动一个测试消费者。测试消费者会把收到的消息存储在内存中，以便测试时验证。

```

@RocketMQTestListener(topics = "topic-example", tags = "msg_tag")
public class ExampleConsumerListener implements RocketListener<MessageData> { ... }
```

在测试用例中，您可以使用 RocketMQTestListener.getMessages() 方法获取收到的消息。

## 11. 性能调优

为了提高系统性能，您可以通过以下方式调优 RocketMQ：

-   调整线程池大小。
-   优化网络参数，如连接超时时间。
-   调整消费者拉取批次大小。
-   优化消息堆积参数。

具体参数配置请参考 [RocketMQ 官方文档](https://rocketmq.apache.org/docs/tuning/)。

## 12. 社区与支持

-   有问题请在 [码云](https://gitee.com/zc_oss/rocketONS-starter/issues) 提交 issue。
-   欢迎参与项目开发，可通过 Fork 仓库并提交 Pull Request。
-   更多信息请关注作者 [码云主页](https://gitee.com/zc_oss)。