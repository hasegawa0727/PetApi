package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.PetCreateRequest;
import com.example.demo.dto.PetCreateRequest.TagCreateRequest;
import com.example.demo.dto.PetResponse;
import com.example.demo.dto.PetUpdateRequest;
import com.example.demo.dto.PetUpdateRequest.TagUpdateRequest;
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
        // カテゴリ情報を取得
        Category category = petMapper.findCategoryByPetId(pet.getId());

        // 写真URLリストを取得
        List<PhotoUrl> photoUrls = petMapper.findPhotoUrlByPetId(pet.getId());
        List<String> urls = photoUrls.stream()
                .map(PhotoUrl::getUrl)
                .toList();

        // タグリストを取得
        List<Tag> tags = petMapper.findTagByPetId(pet.getId());
        List<PetResponse.Tag> tagDtos = tags.stream()
                .map(tag -> new PetResponse.Tag()
                        .setId(tag.getId())
                        .setName(tag.getName()))
                .toList();

        // チェーンセッターを使用してPetResponseを構築
        PetResponse petResponse = new PetResponse()
                .setId(pet.getId())
                .setName(pet.getName())
                .setStatus(pet.getStatus())
                .setPhotoUrls(urls)
                .setTags(tagDtos);

        // カテゴリが存在する場合のみ設定
        if (category != null) {
            petResponse.setCategory(new PetResponse.Category()
                    .setId(category.getId())
                    .setName(category.getName()));
        }

        return petResponse;
    }

    private int resolveCategoryId(String categoryName) {
        Category existingCategory = petMapper.selectCategoryByName(categoryName);

        if (existingCategory != null) {
            return existingCategory.getId();
        } else {
            Category newCategory = new Category();
            newCategory.setName(categoryName);
            petMapper.insertCategory(newCategory);
            return newCategory.getId();
        }
    }

    private int resolveTagId(String tagName) {
        Tag existingTag = petMapper.selectTagByName(tagName);

        if (existingTag != null) {
            return existingTag.getId();
        } else {
            Tag newTag = new Tag();
            newTag.setName(tagName);
            petMapper.insertTag(newTag);
            return newTag.getId();
        }
    }

    public PetResponse getPetById(int id) {
        Pet pet = petMapper.findById(id);
        if (pet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "指定されたIDのペットは存在しません。");
        }
        return convertToPetResponse(pet);
    }

    public List<PetResponse> getPetList() {
        // List<Pet> pets = petMapper.findPetAll();
        // List<PetResponse> petResponses = new ArrayList<>();
        // for(Pet pet : pets) {
        // petResponses.add(convertToPetResponse(pet));
        // }
        // return petResponses;

        return petMapper.findPetAll()
                .stream()
                .peek(System.out::println)
                .map(this::convertToPetResponse)
                .toList();
    }

    public List<PetResponse> getPetByStatus(String status) {
        List<Pet> pets = petMapper.findPetByStatus(status);
        List<PetResponse> petResponses = new ArrayList<>();
        for (Pet pet : pets) {
            petResponses.add(convertToPetResponse(pet));
        }
        return petResponses;
    }

    public List<PetResponse> getPetByTag(String tag) {
        List<Pet> pets = petMapper.findPetByTag(tag);
        List<PetResponse> petResponses = new ArrayList<>();
        for (Pet pet : pets) {
            petResponses.add(convertToPetResponse(pet));
        }
        return petResponses;
    }

    public PetResponse doPost(PetCreateRequest petCreateRequest) {
        Pet pet = new Pet();
        pet.setName(petCreateRequest.getName());
        pet.setStatus(PetStatus.valueOf("available"));

        if (petCreateRequest.getCategory() != null) {
            int categoryId = resolveCategoryId(petCreateRequest.getCategory().getName());
            pet.setCategoryId(categoryId);
        }

        petMapper.insertPet(pet);

        for (String url : petCreateRequest.getPhotoUrls()) {
            PhotoUrl photoUrl = new PhotoUrl();
            photoUrl.setPetId(pet.getId());
            photoUrl.setUrl(url);
            petMapper.insertPhotoUrl(photoUrl);
        }

        for (TagCreateRequest tagCreateRequest : petCreateRequest.getTags()) {
            int tagId = resolveTagId(tagCreateRequest.getName());
            petMapper.insertPetTag(pet.getId(), tagId);
        }

        return convertToPetResponse(pet);

    }

    public PetResponse doUpdate(int id, PetUpdateRequest petUpdateRequest) {
        Pet pet = petMapper.findById(id);

        if (pet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "指定されたIDのペットは存在しません。");
        }

        if (petUpdateRequest.getName() != null) {
            pet.setName(petUpdateRequest.getName());
        }
        if (petUpdateRequest.getStatus() != null) {
            pet.setStatus(petUpdateRequest.getStatus());
        }

        pet.setId(id);

        if (petUpdateRequest.getCategory() != null) {
            int categoryId = resolveCategoryId(petUpdateRequest.getCategory().getName());
            pet.setCategoryId(categoryId);
        }

        petMapper.updatePet(pet);

        if (petUpdateRequest.getPhotoUrls() != null) {
            petMapper.deletePhotoUrl(id);

            for (String url : petUpdateRequest.getPhotoUrls()) {
                PhotoUrl photoUrl = new PhotoUrl();
                photoUrl.setPetId(id);
                photoUrl.setUrl(url);
                petMapper.insertPhotoUrl(photoUrl);
            }
        }

        if (petUpdateRequest.getTags() != null) {
            petMapper.deletePetTag(id);

            for (TagUpdateRequest tagUpdateRequest : petUpdateRequest.getTags()) {
                int tagId = resolveTagId(tagUpdateRequest.getName());
                petMapper.insertPetTag(pet.getId(), tagId);
            }
        }

        return convertToPetResponse(pet);

    }

    public void doDelete(int id) {
        Pet pet = petMapper.findById(id);
        if (pet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "指定されたIDのペットは存在しません。");
        }

        petMapper.deletePetTag(id);
        petMapper.deletePhotoUrl(id);
        petMapper.deletePet(id);

    }
}