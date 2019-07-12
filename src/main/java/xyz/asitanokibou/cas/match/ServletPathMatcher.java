package xyz.asitanokibou.cas.match;

/**
 * 代码来自alibaba druid的ServletPathMatcher
 */
public class ServletPathMatcher implements PatternMatcher {

    private static final ServletPathMatcher INSTANCE = new ServletPathMatcher();

    public ServletPathMatcher() {
    }

    public static final String ASTERISK = "*";

    public static ServletPathMatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean matches(String pattern, String source) {
        if (pattern != null && source != null) {
            pattern = pattern.trim();
            source = source.trim();
            int start;
            if (pattern.endsWith(ASTERISK)) {
                start = pattern.length() - 1;
                return source.length() >= start && pattern.substring(0, start).equals(source.substring(0, start));
            } else if (pattern.startsWith(ASTERISK)) {
                start = pattern.length() - 1;
                return source.length() >= start && source.endsWith(pattern.substring(1));
            } else if (pattern.contains(ASTERISK)) {
                start = pattern.indexOf(ASTERISK);
                int end = pattern.lastIndexOf(ASTERISK);
                return source.startsWith(pattern.substring(0, start)) && source.endsWith(pattern.substring(end + 1));
            }
            return pattern.equals(source);

        } else {
            return false;
        }
    }
}
