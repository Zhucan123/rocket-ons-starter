package io.gitee.zhucan123.rocketonsspringbootstarter.consumer;

import io.gitee.zhucan123.rocketonsspringbootstarter.RocketAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({RocketAutoConfiguration.class})
public @interface EnableRocketONS {
}