package pl.wylezek.petclinic.petservice.service;

import org.springframework.stereotype.Service;
import pl.wylezek.petclinic.petservice.exceptions.custom.NotFoundEntityException;
import pl.wylezek.petclinic.petservice.model.Pet;
import pl.wylezek.petclinic.petservice.repository.PetRepository;

import java.util.List;

@Service
public class PetService {

    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet getPetById(Long id) {
        return this.petRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(Pet.class));
    }

    public List<Pet> getPetByCategory(String categoryName) {
        return this.petRepository.getByCategory_Name(categoryName);
    }

    public Pet savePet(Pet pet) {
        return this.petRepository.save(pet);
    }

    public void deletePet(Pet pet) {
        this.petRepository.delete(pet);
    }
}
