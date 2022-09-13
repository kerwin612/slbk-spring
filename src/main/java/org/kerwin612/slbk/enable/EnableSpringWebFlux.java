package org.kerwin612.slbk.enable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.isBlank;
import static org.kerwin612.slbk.SLBK.put;

@Order(1)
@Component
public class EnableSpringWebFlux extends RequestMappingHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EnableSpringWebMvc.class);

    @Autowired(required = false)
    EnableByAOP4LogKey enableLogKey;

    @Autowired(required = false)
    GetFilePath getFilePath;

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableSpringWebFlux enabled!");
        }
    }

    @Override
    public Mono<HandlerResult> handle(ServerWebExchange exchange, Object handler) {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod h = (HandlerMethod) handler;
                final Method method = h.getMethod();
                if (getFilePath == null) {
                    getFilePath = ((mthd, args) ->
                            String.format("request/%s/%s", mthd.getDeclaringClass().getSimpleName(), mthd.getName()));
                }
                String logKey = null;
                if (enableLogKey != null) {
                    logKey = enableLogKey.getLogKey(method);
                }
                put(isBlank(logKey) ? getFilePath.get(h.getMethod(), h.getMethodParameters()) : logKey);
            }
            return super.handle(exchange, handler);
        } finally {
            clear();
        }
    }

    public interface GetFilePath {
        String get(Method method, Object[] args);
    }

}
