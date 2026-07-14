package com.example.middleware.feature.security.infrastructure;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";

    private static final String API_KEY = "middleware-secret";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey =
                request.getHeader(API_KEY_HEADER);

        if (!API_KEY.equals(apiKey)) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Invalid API Key");

            return;
        }

        filterChain.doFilter(request, response);
        String uri = request.getRequestURI();

if (!uri.equals("/api/v1/events")) {

    filterChain.doFilter(request, response);

    return;
}
    }
}
