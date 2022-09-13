package org.kerwin612.slbk.enable;

import org.kerwin612.slbk.SLBKProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.put;

@Order(1)
@Component
public class EnableServletFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(EnableServletFilter.class);

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableServletFilter enabled!");
        }
    }

    @Autowired(required = false)
    GetFilePath getFilePath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (getFilePath == null) {
            getFilePath = req1 -> req1.getHeader(SLBKProperties.filterKey());
        }
        put(getFilePath.get(req));
        try {
            chain.doFilter(request, response);
        } finally {
            clear();
        }
    }

    @Override
    public void destroy() {
    }

    interface GetFilePath {
        String get(HttpServletRequest req);
    }

}
