package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PetResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Pet;
import com.example.demo.entity.PhotoUrl;
import com.example.demo.entity.Tag;
import com.example.demo.mapper.PetMapper;


@Service
public class PetService {
    @Autowired
    PetMapper petMapper;
    
    private PetResponse convertToPetResponse(Pet pet) {
        PetResponse petResponse = new PetResponse();
        petResponse.setId(pet.getId());
        petResponse.setName(pet.getName());
        petResponse.setStatus(pet.getStatus());
        
        
        Category category = petMapper.findCategoryByPetId(pet.getId());
        if(category != null) {
            PetResponse.Category categoryDto = new PetResponse.Category(); 
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            petResponse.setCategory(categoryDto);
        }
        
        
        List<PhotoUrl> photoUrlEntity = petMapper.findPhotoUrlByPetId(pet.getId());
        List<String> urls = photoUrlEntity.stream()
                .map(photoUrl -> photoUrl.getUrl())
                .toList();
        petResponse.setPhotoUrls(urls);
        
        
        List<Tag> tags = petMapper.findTagByPetId(pet.getId());
        List<PetResponse.Tag> tagDtos = new ArrayList<>();
        for(Tag tag : tags) {
            PetResponse.Tag tagDto = new PetResponse.Tag();
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
            tagDtos.add(tagDto);
        }
        petResponse.setTags(tagDtos);
        
        return petResponse;
        
    }


    
    public PetResponse getPetById(int id) {
        Pet pet = petMapper.findById(id);
        return convertToPetResponse(pet);
    }
    
    
    public List<PetResponse> getPetList() {
        List<Pet> pets = petMapper.findPetAll();
        List<PetResponse> petResponses = new ArrayList<>();
        for(Pet pet : pets) {
            petResponses.add(convertToPetResponse(pet));
        }
        return petResponses;
    }
    
    
    public List<PetResponse> getPetByStatus(String status) {
        List<Pet> pets = petMapper.findPetByStatus(status);
        List<PetResponse> petResponses = new ArrayList<>();
        for(Pet pet : pets) {
            petResponses.add(convertToPetResponse(pet));
        }
        return petResponses;
    }
    
    public List<PetResponse> getPetByTag(String tag) {
        List<Pet> pets = petMapper.findPetByTag(tag);
        List<PetResponse> petResponses = new ArrayList<>();
        for(Pet pet : pets) {
            petResponses.add(convertToPetResponse(pet));
        }
        return petResponses;
    }
}
