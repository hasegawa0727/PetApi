package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Category;
import com.example.demo.entity.Pet;
import com.example.demo.entity.PhotoUrl;
import com.example.demo.entity.Tag;



@Mapper
public interface PetMapper {
    Pet findById(int id);
    Category findCategoryByPetId(int petId);
    List<PhotoUrl> findPhotoUrlByPetId(int petId);
    List<Tag> findTagByPetId(int petId);
    List<Pet> findPetAll();
    List<Pet> findPetByStatus(String status);
    List<Pet> findPetByTag(String tag);
}
