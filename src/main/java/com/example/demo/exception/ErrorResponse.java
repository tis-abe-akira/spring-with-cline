package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * エラーレスポンスを表現するクラス
 */
public class ErrorResponse {
    @JsonProperty("status")
    private final int status;

    @JsonProperty("message")
    private final String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
