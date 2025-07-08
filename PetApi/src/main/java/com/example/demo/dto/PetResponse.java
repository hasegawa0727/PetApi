package com.example.demo.dto;

import java.util.List;

import com.example.demo.enums.PetStatus;

import lombok.Data;

@Data
public class PetResponse {
    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private PetStatus status;

    @Data
    public static class Category {
        private int id;
        private String name;
    }

    @Data
    public static class Tag {
        private int id;
        private String name;
    }
}
