package com.example.demo.dto;

import java.util.List;

import lombok.Data;


@Data
public class PetCreateRequest {
    private CategoryRequest category;
    private String name;
    private List<String> photoUrls;
    private List<TagRequest> tags;
    
    
    @Data
    public static class CategoryRequest {
        private String name;
    }
    
    
    @Data
    public static class TagRequest {
        private String name;
    }
}
