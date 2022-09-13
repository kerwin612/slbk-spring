package org.kerwin612.slbk.enable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.get;
import static org.kerwin612.slbk.SLBK.isBlank;
import static org.kerwin612.slbk.SLBK.isNotBlank;
import static org.kerwin612.slbk.SLBK.put;

@Order(1)
@Component
public class EnableSpringWebMvc implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(EnableSpringWebMvc.class);

    @Autowired(required = false)
    EnableByAOP4LogKey enableLogKey;

    @Autowired(required = false)
    GetFilePath getFilePath;

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableSpringWebMvc enabled!");
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if (ex != null) {
            String logKey = get();
            if (isNotBlank(logKey)) {
                logger.error(logKey + " error: ", ex);
            }
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        clear();
    }

    public interface GetFilePath {
        String get(Method method, Object[] args);
    }

}
