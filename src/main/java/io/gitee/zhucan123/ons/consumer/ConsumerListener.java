package io.gitee.zhucan123.ons.consumer;

import com.aliyun.openservices.ons.api.PropertyValueConst;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: zhuCan
 * @date: 2019/11/1 10:13
 * @description: 需要注册到rocket 的消费者标记
 */
@Documented
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumerListener {

    /**
     * consumer 注册的 接受消息的过滤tag
     *
     * @return
     */
    String[] tags();

    /**
     * consumer 启动的实例数
     *
     * @return
     */
    int consumers() default 1;

  /**
   * 容器名
   *
   * @return
   */
  String value() default "";

  /**
   * 消费模式
   */
  String pattern() default PropertyValueConst.CLUSTERING;

}
