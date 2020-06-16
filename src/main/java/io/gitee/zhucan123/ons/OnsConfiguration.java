package io.gitee.zhucan123.ons;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: zhuCan
 * @date: 2020/3/17 10:59
 * @description: 消费者和生产者的 topic相关配置,支持${propertyKey} 读取配置文件属性
 */
@Component
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnsConfiguration {

  /**
   * 绑定的主题
   *
   * @return
   */
  String topic();

  /**
   * 绑定的消费组
   *
   * @return
   */
  String group();

  /**
   * 是否开启消费者
   * 因为要支持读取配置文件,所以使用字符串类型
   * @return
   */
  String enable() default "on";
}
