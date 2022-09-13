package org.kerwin612.slbk;

import org.slf4j.MDC;

public final class SLBK {

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static void put(String flag) {
        if (isNotBlank(flag) && isNotBlank(SLBKProperties.filterKey())) {
            MDC.put(SLBKProperties.filterKey(), flag);
        }
    }

    public static String get() {
        return isNotBlank(SLBKProperties.filterKey())
                ? MDC.get(SLBKProperties.filterKey())
                : null;
    }

    public static void clear() {
        MDC.clear();
    }

}
