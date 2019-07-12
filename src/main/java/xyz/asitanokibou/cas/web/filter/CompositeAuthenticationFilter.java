package xyz.asitanokibou.cas.web.filter;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import xyz.asitanokibou.cas.web.filter.CompositeAuthenticationFilter.AuthenticationFilterSupport;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.util.Objects;

/**
 * @author aimysaber@gmail.com
 */
public class CompositeAuthenticationFilter extends AbstractServerBasedCompositeCasFilter<AuthenticationFilterSupport> {

    @Override
    protected AuthenticationFilterSupport createInnerFilter(String serverName, FilterConfig config, ServletRequest request) throws ServletException {
        AuthenticationFilterSupport authenticationFilterSupport = new AuthenticationFilterSupport(serverName, config);
        authenticationFilterSupport.init(config);
        return authenticationFilterSupport;
    }

    class AuthenticationFilterSupport extends AuthenticationFilter {
        final String serverName;

        AuthenticationFilterSupport(String serverName, FilterConfig filterConfig) throws ServletException {
            this.serverName = Objects.requireNonNull(serverName);
        }

        @Override
        protected void initInternal(FilterConfig filterConfig) throws ServletException {
            super.initInternal(filterConfig);
            this.setServerName(serverName);
        }
    }
}
 