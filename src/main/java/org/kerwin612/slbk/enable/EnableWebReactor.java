package org.kerwin612.slbk.enable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class EnableWebReactor {

    @Configuration
    @ConditionalOnClass(WebFluxConfigurer.class)
    class ForWebFlux implements WebFluxConfigurer {

//        @Bean
//        EnableReactorFilter reactorFilter() {
//            return new EnableReactorFilter();
//        }
//
//        @Bean
//        EnableSpringWebFlux springWebFlux() {
//            return new EnableSpringWebFlux();
//        }

        @Bean
        EnableByAOP4SpringWebFlux springWebFlux() {
            return new EnableByAOP4SpringWebFlux();
        }

        @Aspect
        public class EnableByAOP4SpringWebFlux extends EnableByAOP {

            private final Logger logger = LoggerFactory.getLogger(EnableByAOP4SpringWebFlux.class);

            @Autowired(required = false)
            EnableByAOP4LogKey enableLogKey;

            @PostConstruct
            void init() {
                if (logger.isDebugEnabled()) {
                    logger.debug("EnableByAOP4SpringWebFlux enabled!");
                }
            }

            @Around(
                    "@annotation(org.springframework.web.bind.annotation.GetMapping)"
                            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
                            + "|| @annotation(org.springframework.web.bind.annotation.PostMapping)"
                            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
                            + "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
            public Object request(ProceedingJoinPoint joinPoint) throws Throwable {
                return super.aop(
                        joinPoint,
                        (method, args) ->
                                String.format(
                                        "request/%s/%s", method.getDeclaringClass().getSimpleName(), method.getName()), enableLogKey);
            }
        }

    }

}