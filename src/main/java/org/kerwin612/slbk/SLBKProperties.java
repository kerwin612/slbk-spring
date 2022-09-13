package org.kerwin612.slbk;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties(prefix = "slbk")
public class SLBKProperties {

    private static SLBKProperties instance;

    Boolean enable = true;

    String filterKey = "log_key";

    @PostConstruct
    void init() {
        instance = this;
    }

    public static boolean enable() {
        return instance == null ? false : instance.enable;
    }

    public static String filterKey() {
        return instance == null ? null : instance.filterKey;
    }

}
