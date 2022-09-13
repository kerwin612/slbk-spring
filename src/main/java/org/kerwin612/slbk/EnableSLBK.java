package org.kerwin612.slbk;

import org.kerwin612.slbk.enable.EnableByAOP4LogKey;
import org.kerwin612.slbk.enable.EnableByAOP4Scheduled;
import org.kerwin612.slbk.enable.EnableHystrix;
import org.kerwin612.slbk.enable.EnableWebReactor;
import org.kerwin612.slbk.enable.EnableWebServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableSLBK.SLBTAutoConfiguration.class)
public @interface EnableSLBK {

    @Configuration
    @ConditionalOnProperty(
            prefix = "slbk",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    @Import({EnableHystrix.class, EnableWebServlet.class, EnableWebReactor.class})
    @EnableConfigurationProperties({SLBKProperties.class})
    class SLBTAutoConfiguration {

        @Bean
        EnableByAOP4LogKey logKey() {
            return new EnableByAOP4LogKey();
        }

        @Bean
        EnableByAOP4Scheduled scheduled() {
            return new EnableByAOP4Scheduled();
        }

    }

}
