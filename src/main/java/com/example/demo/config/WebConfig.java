package com.example.demo.config;

import com.example.demo.security.interceptor.ApiKeyInterceptor;
import com.example.demo.security.interceptor.HeaderEscapeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVCの設定クラス
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ApiKeyInterceptor apiKeyInterceptor;
    private final HeaderEscapeInterceptor headerEscapeInterceptor;

    public WebConfig(ApiKeyInterceptor apiKeyInterceptor, HeaderEscapeInterceptor headerEscapeInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
        this.headerEscapeInterceptor = headerEscapeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // ヘッダーエスケープは全てのリクエストに適用
        registry.addInterceptor(headerEscapeInterceptor)
                .addPathPatterns("/**")
                .order(1);  // 最初に実行

        // APIキー認証はAPIエンドポイントのみに適用
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**")
                .order(2);  // ヘッダーエスケープの後に実行
    }
}
