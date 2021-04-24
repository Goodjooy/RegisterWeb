package com.jacky.register.security.filters;

import com.jacky.register.security.utils.XSSUtils;
import com.jacky.register.security.wrappers.XSSRequestWrapper;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.OrderBy;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class XssFilter implements Filter {

    /**
     * The <code>doFilter</code> method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due to a
     * client request for a resource at the end of the chain. The FilterChain
     * passed in to this method allows the Filter to pass on the request and
     * response to the next entity in the chain.
     * <p>
     * A typical implementation of this method would follow the following
     * pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering <br>
     * 3. Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering <br>
     * 4. a) <strong>Either</strong> invoke the next entity in the chain using
     * the FilterChain object (<code>chain.doFilter()</code>), <br>
     * 4. b) <strong>or</strong> not pass on the request/response pair to the
     * next entity in the filter chain to block the request processing<br>
     * 5. Directly set headers on the response after invocation of the next
     * entity in the filter chain.
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this
     *                 filter to pass the request and response to for further
     *                 processing
     * @throws IOException      if an I/O error occurs during this filter's
     *                          processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        XSSRequestWrapper requestWrapper=new XSSRequestWrapper((HttpServletRequest) request);

        String body= IOUtils.toString(requestWrapper.getReader());

        if(!StringUtils.isEmpty(body))
        {
            body= XSSUtils.stripXSS(body);
            requestWrapper.resetInputStream(body.getBytes());
        }

        chain.doFilter(requestWrapper,response);
    }
}
