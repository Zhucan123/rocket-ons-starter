package io.gitee.zhucan123.ons;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import io.gitee.zhucan123.ons.consumer.ConsumerListener;
import io.gitee.zhucan123.ons.consumer.RocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author: zhuCan
 * @date: 2020/4/19 19:41
 * @description: rocket 的消费者自动注册
 * 使用容器工厂扫描所有consumer, 并根据注解配置属性 subscribe到相应的队列
 */
public class ConsumerAutoRegister {

  @Autowired
  private RocketProperties configuration;

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private PropertyResolver propertyResolver;

  protected Logger logger = LoggerFactory.getLogger(this.getClass());


  /**
   * 用来注册consumer的
   */
  @PostConstruct
  public void consumerListenerRegister() {
    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(ConsumerListener.class);


    List<RocketListener<?>> rocketListeners = new ArrayList<>();

    Arrays.stream(beanNamesForAnnotation)
        .map(x -> (RocketListener<?>) autowireCapableBeanFactory.getBean(x))
        .forEach(x -> {
          ConsumerListener consumerListener = x.getClass().getAnnotation(ConsumerListener.class);
          OnsConfiguration config = x.getClass().getAnnotation(OnsConfiguration.class);
          if ("on".equalsIgnoreCase(config.enable())) {
            for (int i = 0; i < consumerListener.consumers(); i++) {
              rocketListeners.add(x);
            }
          }
        });


    // 启动一个定时器,延时加载,
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        listenerRegister(rocketListeners.toArray(new RocketListener[0]));
      }
    }, configuration.getDelay());


  }

  /**
   * 注册consumerListener
   *
   * @param listener
   */
  private void listenerRegister(RocketListener<?>... listener) {
    Arrays.stream(listener).forEach(x -> {
      // 是否开启注册
      Properties properties = configuration.rocketProperties();
      // 获取注册注解
      ConsumerListener consumerListener = x.getClass().getAnnotation(ConsumerListener.class);
      OnsConfiguration config = x.getClass().getAnnotation(OnsConfiguration.class);
      properties.put(PropertyKeyConst.GROUP_ID, configuration.getGroupSuffix() + propertyResolver.springElResolver(config.group()));
      properties.put(PropertyKeyConst.MessageModel, propertyResolver.springElResolver(consumerListener.pattern()));
      Consumer consumer = ONSFactory.createConsumer(properties);
      // 注册消费者监听器
      consumer.subscribe(propertyResolver.springElResolver(config.topic()), String.join("||", consumerListener.tags()), x);
      // 启动消费者
      logger.info("启动消费者: {}", properties.get(PropertyKeyConst.GROUP_ID));
      consumer.start();

    });

  }

}
