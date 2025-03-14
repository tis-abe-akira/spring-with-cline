package com.example.demo.service;

import com.example.demo.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemService {

    public void registerItem(Item item) {
        // 本来はDB登録などの処理を行うが、今回はログ出力のみ
        log.info("Item registered: {}", item);
    }
}
