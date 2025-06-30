package com.example.demo.dto;

import java.util.List;

import lombok.Data;


@Data
public class PetCreateRequest {
    private CategoryCreateRequest category;
    private String name;
    private List<String> photoUrls;
    private List<TagCreateRequest> tags;
    
    
    @Data
    public static class CategoryCreateRequest {
        private String name;
    }
    
    
    @Data
    public static class TagCreateRequest {
        private String name;
    }
}
