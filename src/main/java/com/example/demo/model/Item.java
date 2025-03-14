package com.example.demo.model;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class Item {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1024)
    private String description;
}
