package io.gitee.zhucan123.ons;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({RocketAutoConfiguration.class})
public @interface EnableRocketONS {
}