package io.gitee.zhucan123.rocketonsspringbootstarter;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import io.gitee.zhucan123.rocketonsspringbootstarter.consumer.ConsumerListener;
import io.gitee.zhucan123.rocketonsspringbootstarter.consumer.RocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * generator for rocket mq !
 * This codes are generated automatically. Do not modify!
 * -.-
 * created by zhuCan
 */
@Component
public class ConsumerAutoRegister {

  private final RocketConfiguration configuration;

  private final ApplicationContext applicationContext;

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  public ConsumerAutoRegister(RocketConfiguration configuration, ApplicationContext applicationContext) {
    this.configuration = configuration;
    this.applicationContext = applicationContext;
  }

  /**
   * 用来注册consumer的
   */
  @PostConstruct
  public void consumerListenerRegister() {
    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(ConsumerListener.class);


    List<RocketListener> rocketListeners = new ArrayList<>();

    Arrays.stream(beanNamesForAnnotation)
        .map(x -> (RocketListener) autowireCapableBeanFactory.getBean(x))
        .filter(TopicManager::getEnable)
        .forEach(x -> {
          int consumers = x.getClass().getAnnotation(ConsumerListener.class).consumers();
          for (int i = 0; i < consumers; i++) {
            rocketListeners.add(x);
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
  private void listenerRegister(RocketListener... listener) {
    Arrays.stream(listener).forEach(x -> {
      // 是否开启注册
      Properties properties = configuration.rocketProperties();
      // 获取注册注解
      ConsumerListener consumerListener = x.getClass().getAnnotation(ConsumerListener.class);
      properties.put(PropertyKeyConst.GROUP_ID, x.getGroup());
      Consumer consumer = ONSFactory.createConsumer(properties);
      // 注册消费者监听器;
      consumer.subscribe(x.getTopic(), String.join("||", consumerListener.tags()), x);
      // 启动消费者;
      logger.info("启动消费者: {}", properties.get(PropertyKeyConst.GROUP_ID));
      consumer.start();

    });

  }
}
