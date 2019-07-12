package xyz.asitanokibou.cas.web.filter;

import xyz.asitanokibou.cas.web.filter.CompositeTicketValidationFilter.CasTicketValidationFilterSupport;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.util.Objects;


/**
 * @author aimysaber@gmail.com
 */
public class CompositeTicketValidationFilter extends AbstractServerBasedCompositeCasFilter<CasTicketValidationFilterSupport> {

    @Override
    protected CasTicketValidationFilterSupport createInnerFilter(String serverName, FilterConfig config, ServletRequest request)
            throws ServletException {
        CasTicketValidationFilterSupport casTicketValidationFilterSupport = new CasTicketValidationFilterSupport(serverName, config);
        casTicketValidationFilterSupport.init(config);
        return casTicketValidationFilterSupport;
    }

    class CasTicketValidationFilterSupport extends org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter {
        final String serverName;

        CasTicketValidationFilterSupport(String serverName, FilterConfig filterConfig) {
            this.serverName = Objects.requireNonNull(serverName);
        }

        @Override
        protected void initInternal(FilterConfig filterConfig) throws ServletException {
            super.initInternal(filterConfig);
            //因为后续的init()方法会检测 serverName是否为空 这里需要设置下
            this.setServerName(serverName);
        }

        /*@Override
        protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response,
                                              Assertion assertion) {
            super.onSuccessfulValidation(request, response, assertion);
        }*/
    }
}
 