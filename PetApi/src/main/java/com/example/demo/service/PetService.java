package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PetCreateRequest;
import com.example.demo.dto.PetCreateRequest.CategoryRequest;
import com.example.demo.dto.PetCreateRequest.TagRequest;
import com.example.demo.dto.PetResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Pet;
import com.example.demo.entity.PhotoUrl;
import com.example.demo.entity.Tag;
import com.example.demo.enums.PetStatus;
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
        
        
        List<PhotoUrl> photoUrls = petMapper.findPhotoUrlByPetId(pet.getId());
        List<String> urls = photoUrls.stream()
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
    
    
    public PetResponse doPost(PetCreateRequest petCreateRequest) {
        Pet pet = new Pet();
        pet.setName(petCreateRequest.getName());
        pet.setStatus(PetStatus.valueOf("available"));
        
        CategoryRequest category = petCreateRequest.getCategory();
        Category existingCategory = petMapper.selectCategoryByName(category.getName());
            
            int categoryId;
            if(existingCategory != null) {
                categoryId = existingCategory.getId();
            } else {
                Category newCategory = new Category();
                newCategory.setName(category.getName());
                petMapper.insertCategory(newCategory);
                categoryId = newCategory.getId();
            }
            
            pet.setCategoryId(categoryId);
        
        petMapper.insertPet(pet);
        
        
        for(String url : petCreateRequest.getPhotoUrls()) {
            PhotoUrl photoUrl = new PhotoUrl();
            photoUrl.setPetId(pet.getId());
            photoUrl.setUrl(url);
            petMapper.insertPhotoUrl(photoUrl);
        }
        
        for(TagRequest tagRequest : petCreateRequest.getTags()) {
            Tag existingTag = petMapper.selectTagByName(tagRequest.getName());
            
            int tagId;
            if(existingTag != null) {
                tagId = existingTag.getId();
            } else {
                Tag newTag = new Tag();
                newTag.setName(tagRequest.getName());
                petMapper.insertTag(newTag);
                tagId = newTag.getId();
            }
            
            petMapper.insertPetTag(pet.getId(), tagId);
            
        }
        
        return convertToPetResponse(pet);
            
    }
}
