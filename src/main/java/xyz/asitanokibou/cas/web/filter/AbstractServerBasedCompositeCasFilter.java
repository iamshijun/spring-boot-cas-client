package xyz.asitanokibou.cas.web.filter;

import xyz.asitanokibou.cas.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aimysaber@gmail.com
 * @param <T>
 */
public abstract class AbstractServerBasedCompositeCasFilter<T extends Filter> extends AbstractCompositeCasFilter<String, T> {

	@Override
	protected void doInternalFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		this.getInnerFilter(request, WebUtils.getBasePath(request))
				.doFilter(request, response, filterChain);

	}

}
