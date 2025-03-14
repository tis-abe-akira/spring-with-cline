package com.example.demo.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * フィルター設定クラス
 */
@Configuration
public class SafeHeaderConfig {
    private static final Logger logger = LoggerFactory.getLogger(SafeHeaderConfig.class);

    private final HeaderValidationFilter headerValidationFilter;

    public SafeHeaderConfig(HeaderValidationFilter headerValidationFilter) {
        this.headerValidationFilter = headerValidationFilter;
    }

    @Bean
    public FilterRegistrationBean<HeaderValidationFilter> headerValidationFilterRegistration() {
        FilterRegistrationBean<HeaderValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(headerValidationFilter);
        registrationBean.addUrlPatterns("/api/*"); // フィルターを適用するURLパターン
        logger.info("HeaderValidationFilter registered for URL pattern: /api/*");
        return registrationBean;
    }
}
