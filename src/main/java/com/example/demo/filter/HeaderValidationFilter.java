package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.exception.ValidationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * HTTPヘッダーのバリデーションを行うフィルター
 * x-api-keyヘッダーを使用してAPIアクセスを認証する
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HeaderValidationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(HeaderValidationFilter.class);

    private final HeaderValidator headerValidator;

    @Autowired
    public HeaderValidationFilter(HeaderValidator headerValidator) {
        this.headerValidator = headerValidator;
        logger.info("HeaderValidationFilter initialized");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldFilter = path.startsWith("/api/");
        logger.info("Request path: {}, shouldFilter: {}", path, shouldFilter);
        return !shouldFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 詳細なデバッグログ
        logger.info("=== HeaderValidationFilter.doFilterInternal called ===");
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Content-Type: {}", request.getContentType());
        
        // すべてのヘッダーを出力
        logger.info("=== Request Headers ===");
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            Collections.list(headerNames).forEach(headerName -> 
                logger.info("Header: {} = {}", headerName, request.getHeader(headerName))
            );
        }
        
        // APIキーの検証
        try {
            // リクエストを検証（x-api-keyヘッダーの値を検証）
            logger.info("Validating x-api-key header");
            headerValidator.validate(request);
            
            // デバッグログ
            logger.info("API key validation successful");
            
        } catch (ValidationException e) {
            // バリデーションエラーの場合は400 Bad Requestを返す
            logger.error("Validation error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        } catch (Exception e) {
            // その他の例外の場合は500 Internal Server Errorを返す
            logger.error("Unexpected error during validation: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Internal server error\"}");
            return;
        }

        // 処理を続行
        logger.info("Continuing filter chain");
        filterChain.doFilter(request, response);
    }
}
