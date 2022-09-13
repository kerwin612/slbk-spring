package org.kerwin612.slbk.enable;

import org.kerwin612.slbk.SLBKProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.put;

@Order(1)
@Component
public class EnableReactorFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(EnableReactorFilter.class);

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableReactorFilter enabled!");
        }
    }

    @Autowired(required = false)
    GetFilePath getFilePath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        ServerHttpRequest request = exchange.getRequest();
        if (getFilePath == null) {
            getFilePath = req1 -> req1.getHeaders().getFirst(SLBKProperties.filterKey());
        }
        put(getFilePath.get(request));
        try {
            return chain.filter(exchange).doOnNext(value -> {
                if (map != null) {
                    MDC.setContextMap(map);
                }
            });
        } finally {
            clear();
        }
    }

    interface GetFilePath {
        String get(ServerHttpRequest req);
    }

}
