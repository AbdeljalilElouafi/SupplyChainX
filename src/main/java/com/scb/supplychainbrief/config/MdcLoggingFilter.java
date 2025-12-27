package com.scb.supplychainbrief.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MdcLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        MDC.put("userEmail", (auth != null) ? auth.getName() : "anonymous");
        MDC.put("userRole", (auth != null) ? auth.getAuthorities().toString() : "none");
        MDC.put("method", req.getMethod());
        MDC.put("uri", req.getRequestURI());

        try {
            chain.doFilter(request, response);
        } finally {
            HttpServletResponse res = (HttpServletResponse) response;
            MDC.put("status", String.valueOf(res.getStatus()));

            MDC.clear();
        }
    }
}