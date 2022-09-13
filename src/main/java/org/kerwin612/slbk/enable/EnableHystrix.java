package org.kerwin612.slbk.enable;

import com.netflix.hystrix.Hystrix;
import org.kerwin612.uhcs.HystrixCallableWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.Callable;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.get;
import static org.kerwin612.slbk.SLBK.isBlank;
import static org.kerwin612.slbk.SLBK.put;

@Configuration
@ConditionalOnClass({Hystrix.class})
public class EnableHystrix {

    private static final Logger logger = LoggerFactory.getLogger(EnableHystrix.class);

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableHystrix enabled!");
        }
    }

    @Bean
    HystrixCallableWrapper mdcHystrixCallableWrapper() {
        return new HystrixCallableWrapper() {
            @Override
            public <T> Callable<T> wrap(Callable<T> callable) {
                String log_key = get();
                if (isBlank(log_key)) return callable;
                return () -> {
                    put(log_key);
                    try {
                        return callable.call();
                    } finally {
                        clear();
                    }
                };
            }
        };
    }

}
