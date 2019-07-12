package xyz.asitanokibou.cas.web.filter;

import xyz.asitanokibou.cas.match.PatternMatcher;
import xyz.asitanokibou.cas.match.ServletPathMatcher;
import xyz.asitanokibou.cas.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author aimysaber@gmail.com
 * @param <T>
 */
public abstract class AbstractCompositeCasFilter<K, T extends Filter> implements Filter {

    private final ConcurrentMap<K, T> cacheFilterMap = new ConcurrentHashMap<>();
    private FilterConfig filterConfig;

    public static final String IGNORE_PATTERN_PARAM = "globalIgnorePattern";

    private PatternMatcher pathMatcher;
    private List<String> ignorePatterns;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isRequestUrlExcluded(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        doInternalFilter(request, response, filterChain);
    }

    /**
     * 使用内部filter进行处理
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    protected abstract void doInternalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException;


    /**
     * 创建真正处理请求的内部filter
     * @param key
     * @param config
     * @param request
     * @return
     * @throws ServletException
     */
    protected abstract T createInnerFilter(K key, FilterConfig config, ServletRequest request) throws ServletException;


    protected T getInnerFilter(ServletRequest request, K key) throws ServletException {
        T innerFilter = cacheFilterMap.get(key);

        if (innerFilter == null) {
            cacheFilterMap.putIfAbsent(key, createInnerFilter(key, filterConfig, request));
            innerFilter = cacheFilterMap.get(key);
        }
        return innerFilter;
    }


    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

        ServletContext sc = filterConfig.getServletContext();
        String ignorePattern = sc.getInitParameter(IGNORE_PATTERN_PARAM);
        if (ignorePattern != null && ignorePattern.length() != 0) {
            pathMatcher = ServletPathMatcher.getInstance();
            ignorePatterns = Arrays.asList(ignorePattern.split(","));
        }
    }

    protected boolean isRequestUrlExcluded(final HttpServletRequest request) {
        if (ignorePatterns == null) {
            return false;
        }

        String requestURI = request.getRequestURI();

        String contextPath = request.getContextPath();

        for (String pattern : ignorePatterns) {
            if (pathMatcher.matches(pattern, requestURI)) {
                return true;
            }
            //如果有contextPath将 其添加到pattern中 再次尝试
            if (StringUtils.isNotBlank(contextPath)) {
                String contextPattern;
                if (pattern.startsWith("/")) {
                    contextPattern = contextPath + pattern;
                } else {
                    contextPattern = contextPath + "/" + pattern;
                }
                if (pathMatcher.matches(contextPattern, requestURI)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void destroy() {
        if (!cacheFilterMap.isEmpty()) {
            for (T filter : cacheFilterMap.values()) {
                filter.destroy();
            }
        }
    }
}
