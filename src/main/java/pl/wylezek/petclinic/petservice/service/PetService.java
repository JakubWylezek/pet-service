package pl.wylezek.petclinic.petservice.service;

import org.springframework.stereotype.Service;
import pl.wylezek.petclinic.petservice.exceptions.custom.EmptyEntityListException;
import pl.wylezek.petclinic.petservice.exceptions.custom.EntityAlreadyExistException;
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

    public List<Pet> getPetsByCategory(String categoryName) {
        List<Pet> pets = this.petRepository.getByCategory_Name(categoryName);
        if (pets.isEmpty())
            throw new EmptyEntityListException(Pet.class);
        return pets;
    }

    public Pet savePet(Pet pet) {
        try {
            getPetById(pet.getId());
            throw new EntityAlreadyExistException(Pet.class);
        } catch (NotFoundEntityException e) {
            return this.petRepository.save(pet);
        }
    }

    public Pet updatePet(Pet pet) {
        getPetById(pet.getId());
        return this.petRepository.save(pet);
    }

    public void deletePet(Pet pet) {
        getPetById(pet.getId());
        this.petRepository.delete(pet);
    }
}
