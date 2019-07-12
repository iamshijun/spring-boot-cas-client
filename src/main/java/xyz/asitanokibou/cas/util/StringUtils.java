package xyz.asitanokibou.cas.util;


/**
 * @author aimysaber@gmail.com
 */
public final class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
