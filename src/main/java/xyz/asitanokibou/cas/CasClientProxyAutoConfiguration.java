package xyz.asitanokibou.cas;

import xyz.asitanokibou.cas.util.StringUtils;
import xyz.asitanokibou.cas.web.filter.CompositeAuthenticationFilter;
import xyz.asitanokibou.cas.web.filter.CompositeTicketValidationFilter;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author aimysaber@gmail.com
 */
@Configuration
@ConditionalOnProperty(value = "cas.filter.enabled", matchIfMissing = true)
@EnableConfigurationProperties({CasProperties.class})
public class CasClientProxyAutoConfiguration {

    private CasProperties casProperties;

    public CasClientProxyAutoConfiguration(@Autowired CasProperties casProperties){
        this.casProperties = casProperties;
    }

    @Bean
    public FilterRegistrationBean singleSignOutFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        filterRegistrationBean.setFilter(singleSignOutFilter);
        filterRegistrationBean.setOrder(casProperties.getFilterInitOrder());

        //config: casServerUrlPrefix
        filterRegistrationBean.addInitParameter(
                ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getServerUrlPrefix());

        return filterRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListenerServletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean<>(new SingleSignOutHttpSessionListener());
    }

    @Bean
    public FilterRegistrationBean authenticationFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CompositeAuthenticationFilter authenticationFilter = new CompositeAuthenticationFilter();
        filterRegistrationBean.setFilter(authenticationFilter);
        filterRegistrationBean.setOrder(casProperties.getFilterInitOrder() + 1);

        filterRegistrationBean.addInitParameter("casServerLoginUrl", casProperties.getServerLoginUrl());
        if (StringUtils.isNotBlank(casProperties.getIgnorePattern())) {
            filterRegistrationBean.addInitParameter("ignorePattern", casProperties.getIgnorePattern());
        }
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean ticketValidationFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CompositeTicketValidationFilter ticketValidationFilter = new CompositeTicketValidationFilter();
        filterRegistrationBean.setFilter(ticketValidationFilter);
        filterRegistrationBean.setOrder(casProperties.getFilterInitOrder() + 2);


        String validateUrl = casProperties.getValidateUrl();
        if (StringUtils.isBlank(validateUrl)) {
            validateUrl = casProperties.getServerUrlPrefix();
        }

        filterRegistrationBean.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), validateUrl);


        if (StringUtils.isNotBlank(casProperties.getIgnorePattern())) {
            filterRegistrationBean.addInitParameter("ignorePattern", casProperties.getIgnorePattern());
        }
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        HttpServletRequestWrapperFilter httpServletRequestWrapperFilter = new HttpServletRequestWrapperFilter();

        filterRegistrationBean.setFilter(httpServletRequestWrapperFilter);
        filterRegistrationBean.setOrder(casProperties.getFilterInitOrder() + 3);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean assertionThreadLocalFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        AssertionThreadLocalFilter assertionThreadLocalFilter = new AssertionThreadLocalFilter();

        filterRegistrationBean.setFilter(assertionThreadLocalFilter);
        filterRegistrationBean.setOrder(casProperties.getFilterInitOrder() + 4);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }


}
