package io.gitee.zhucan123.rocketonsspringbootstarter;

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
}
