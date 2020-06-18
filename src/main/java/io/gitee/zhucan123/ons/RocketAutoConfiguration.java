package io.gitee.zhucan123.ons;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: zhuCan
 * @date: 2020/1/10 17:40
 * @description:
 */
@Configuration
@EnableConfigurationProperties(RocketProperties.class)
@ConditionalOnProperty(prefix = "rocket",value = "enable")
@ConditionalOnClass({MessageListener.class, ONSFactory.class})
@Import({ConsumerAutoRegister.class, EnvironmentVirtual.class,PropertyResolver.class})
public class RocketAutoConfiguration {


    @ConditionalOnMissingBean(ConsumerAutoRegister.class)
    public ConsumerAutoRegister consumerAutoRegister() {
        return new ConsumerAutoRegister();
    }
}
