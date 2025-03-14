package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * HTTPヘッダーのバリデーションを行うクラス
 * x-api-keyヘッダーを使用してAPIアクセスを認証する
 */
@Component
public class HeaderValidator {
    private static final Logger logger = LoggerFactory.getLogger(HeaderValidator.class);
    private static final String API_KEY_HEADER = "x-api-key";

    @Value("${api.key}")
    private String expectedApiKey;

    /**
     * リクエストのバリデーションを行う
     * x-api-keyヘッダーの値を検証する
     *
     * @param request HttpServletRequest
     * @throws ValidationException バリデーションエラーの場合
     */
    public void validate(HttpServletRequest request) throws IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);
        
        // x-api-keyヘッダーの存在確認
        if (apiKey == null || apiKey.isEmpty()) {
            logger.warn("Missing required header: {}", API_KEY_HEADER);
            throw new ValidationException(
                    "Missing required header: " + API_KEY_HEADER,
                    HttpStatus.UNAUTHORIZED
            );
        }

        // APIキーの検証
        if (!expectedApiKey.equals(apiKey)) {
            logger.warn("Invalid API key: {}", apiKey);
            throw new ValidationException(
                    "Invalid API key",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // デバッグログ
        logger.info("API key validation successful");
    }
}
