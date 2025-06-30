package com.example.demo.dto;

import java.util.List;

import com.example.demo.enums.PetStatus;

import lombok.Data;

@Data
public class PetUpdateRequest {

    private CategoryUpdateRequest category;
    private String name;
    private List<String> photoUrls;
    private List<TagUpdateRequest> tags;
    private PetStatus status;
        
        
    @Data
    public static class CategoryUpdateRequest {
        private String name;
    }
        
        
    @Data
    public static class TagUpdateRequest {
        private String name;
    }
}

