package io.gitee.zhucan123.ons;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: zhuCan
 * @date: 2020/1/10 17:40
 * @description:
 */
@Configuration
@EnableConfigurationProperties(RocketConfiguration.class)
@Import({ConsumerAutoRegister.class})
public class RocketAutoConfiguration {


  @ConditionalOnMissingBean(ConsumerAutoRegister.class)
  public ConsumerAutoRegister consumerAutoRegister(){
    return new ConsumerAutoRegister();
  }
}
