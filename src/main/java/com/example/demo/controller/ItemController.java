package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Pattern;

/**
 * アイテム関連のAPIエンドポイントを提供するコントローラー
 * コントローラーレベルでの検証を第一義的な防御策として実装
 */
@RestController
@Validated
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 新しいアイテムを作成するエンドポイント
     * 各ヘッダーに対して明示的なバリデーションパターンを適用
     */
    @PostMapping("/api/items")
    @Operation(summary = "新しいアイテムを作成", parameters = {
            @Parameter(in = ParameterIn.HEADER, name = "x-api-key", description = "APIキー", required = true, schema = @Schema(type = "string"))
    })
    public ResponseEntity<Void> createItem(
            @RequestHeader("Authorization") @Pattern(regexp = "^Bearer [A-Za-z0-9-._~+/]+=*$", message = "Authorization header must be a valid Bearer token") String authorization,

            @RequestHeader("deviceUuid") @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "deviceUuid must be a valid UUID") String deviceUuid,

            @RequestHeader("devicePlatform") @Pattern(regexp = "^(iOS|Android)$", message = "devicePlatform must be either iOS or Android") String devicePlatform,

            @RequestBody @Validated Item item) {

        itemService.registerItem(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
