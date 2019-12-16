package pl.wylezek.petclinic.petservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.wylezek.petclinic.petservice.model.Pet;
import pl.wylezek.petclinic.petservice.service.PetService;

import java.util.List;

@RestController
@EnableWebMvc
public class PetController {

    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(value = "/api/v1/pets/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return new ResponseEntity<>(this.petService.getPetById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/pets")
    public ResponseEntity<List<Pet>> getPetsByCategory(@RequestParam(name = "category") String categoryName) {
        return new ResponseEntity<>(this.petService.getPetsByCategory(categoryName), HttpStatus.OK);
    }

    @PostMapping(value = "/api/v1/pets")
    public ResponseEntity<Pet> addNewPet(@RequestBody Pet pet) {
        return new ResponseEntity<>(this.petService.savePet(pet), HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/v1/pets")
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) {
        return new ResponseEntity<>(this.petService.updatePet(pet), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/v1/pets")
    public ResponseEntity deletePet(@RequestBody Pet pet) {
        this.petService.deletePet(pet);
        return ResponseEntity.noContent().build();
    }
}
