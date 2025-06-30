package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PetCreateRequest;
import com.example.demo.dto.PetResponse;
import com.example.demo.service.PetService;


@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PetResponse getPetById(@PathVariable int id) {
        PetResponse pet = petService.getPetById(id);

        return pet;
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponse> getAllPet() {
        return petService.getPetList();
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponse> getPetByStatus(@RequestParam String status) {
        return petService.getPetByStatus(status);
    }
    
    @GetMapping("/tag")
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponse> getPetByTag(@RequestParam String tag) {
        return petService.getPetByTag(tag);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse doPost(@RequestBody PetCreateRequest petCreateRequest) {
        return petService.doPost(petCreateRequest);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void doDelete(@PathVariable int id) {
        petService.doDelete(id);
    }
}
