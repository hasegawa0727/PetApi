package com.example.demo.entity;

import com.example.demo.enums.PetStatus;

import lombok.Data;


@Data
public class Pet {
    private int id;
    private int categoryId;
    private String name;
    private PetStatus status;
}
