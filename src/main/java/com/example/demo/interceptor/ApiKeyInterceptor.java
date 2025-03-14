package com.example.demo.interceptor;

import com.example.demo.filter.HeaderValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * APIキー認証を行うインターセプター
 */
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyInterceptor.class);

    private final HeaderValidator headerValidator;

    public ApiKeyInterceptor(HeaderValidator headerValidator) {
        this.headerValidator = headerValidator;
        logger.info("ApiKeyInterceptor initialized");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("=== ApiKeyInterceptor.preHandle called ===");
        logger.info("Request URI: {}", request.getRequestURI());

        // APIパスの場合のみ検証を実行
        if (request.getRequestURI().startsWith("/api/")) {
            headerValidator.validate(request);
        }

        return true;
    }
}
