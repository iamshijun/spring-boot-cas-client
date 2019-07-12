package xyz.asitanokibou.cas;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author aimysaber@gmail.com
 */
@ConfigurationProperties(prefix = "cas")
public class CasProperties {

    private String serverUrlPrefix;

    private String serverLoginUrl;

    /**
     * cas中的filter所在的开始位置
     */
    private int filterInitOrder = 1;

    /**
     * 如果为空的话 使用 serverLoginUrl
     */
    private String validateUrl;

    /**
     * 忽略的uri样式
     */
    private String ignorePattern;

    public int getFilterInitOrder() {
        return filterInitOrder;
    }

    public void setFilterInitOrder(int filterInitOrder) {
        this.filterInitOrder = filterInitOrder;
    }

    public String getServerUrlPrefix() {
        return serverUrlPrefix;
    }

    public void setServerUrlPrefix(String serverUrlPrefix) {
        this.serverUrlPrefix = serverUrlPrefix;
    }

    public String getServerLoginUrl() {
        return serverLoginUrl;
    }

    public void setServerLoginUrl(String serverLoginUrl) {
        this.serverLoginUrl = serverLoginUrl;
    }

    public String getIgnorePattern() {
        return ignorePattern;
    }

    public void setIgnorePattern(String ignorePattern) {
        this.ignorePattern = ignorePattern;
    }

    public String getValidateUrl() {
        return validateUrl;
    }

    public void setValidateUrl(String validateUrl) {
        this.validateUrl = validateUrl;
    }
}
