package com.tob1.pets;


import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetRepository petRepository;


    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Pet>> searchPets(@RequestParam("name") String name,@RequestParam("description") String description)  {

      var listPetName = petRepository.findByNameLike("%" +name + "%");
      var listPetDescription = petRepository.findByDescriptionLike("%"+description+ "%");
      var list = new ArrayList<Pet>();
      list.addAll(listPetName);
      list.addAll(listPetDescription);
    return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet createdPet = petRepository.save(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + id));
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet updatedPet) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + id));

        pet.setName(updatedPet.getName());
        pet.setDescription(updatedPet.getDescription());

        Pet updatedPetEntity = petRepository.save(pet);
        return new ResponseEntity<>(updatedPetEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}