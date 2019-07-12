package xyz.asitanokibou.cas.match;

public interface PatternMatcher {

    /**
     * 请求是否匹配指定样式
     * @param pattern p
     * @param requestURI uri
     * @return true匹配/false不匹配
     */
    boolean matches(String pattern, String requestURI);
}
