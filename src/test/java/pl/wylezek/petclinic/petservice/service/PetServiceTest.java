package pl.wylezek.petclinic.petservice.service;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.wylezek.petclinic.petservice.exceptions.custom.EmptyEntityListException;
import pl.wylezek.petclinic.petservice.exceptions.custom.EntityAlreadyExistException;
import pl.wylezek.petclinic.petservice.exceptions.custom.NotFoundEntityException;
import pl.wylezek.petclinic.petservice.model.Category;
import pl.wylezek.petclinic.petservice.model.Pet;
import pl.wylezek.petclinic.petservice.repository.PetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PetServiceTest {

    @Autowired
    private PetService petService;

    @MockBean
    private PetRepository petRepository;

    private Pet pet1;
    private Pet pet2;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        pet1 = new Pet(1L, "Dog", 6, new Category());
        pet2 = new Pet(2L, "Cat", 3, new Category());
        pets = List.of(pet1, pet2);
    }

    @Test
    void getPetByIdShouldReturnCorrectPet() {
        //when
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet1));
        //then
        assertEquals(petService.getPetById(1L), pet1);
        verify(petRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(petRepository);
    }

    @Test
    void getPetByIdShouldThrowNotFoundEntityException() {
        //when
        when(petRepository.findById(3L)).thenReturn(Optional.empty());
        //then
        assertThrows(NotFoundEntityException.class, () -> petService.getPetById(3L));
        verify(petRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(petRepository);
    }

    @Test
    void getPetsByCategoryShouldReturnCorrectListOfPets() {
        //when
        when(petRepository.getByCategory_Name("Animals")).thenReturn(pets);
        //then
        assertEquals(pets, petService.getPetsByCategory("Animals"));
        verify(petRepository, times(1)).getByCategory_Name("Animals");
        verifyNoMoreInteractions(petRepository);
    }

    @Test
    void getPetsByCategoryShouldThrowNotFoundEntityException() {
        //when
        when(petRepository.getByCategory_Name("Cows")).thenReturn(List.of());
        //then
        assertThrows(EmptyEntityListException.class, () -> petService.getPetsByCategory("Cows"));
        verify(petRepository, times(1)).getByCategory_Name("Cows");
        verifyNoMoreInteractions(petRepository);
    }

    @Test
    void savePetShouldCorrectlySaveNewPet() {
        //given
        Pet temp = new Pet();
        temp.setId(1L);
        //when
        when(petRepository.save(any())).thenReturn(temp);
        //then
        assertEquals(temp, petService.savePet(new Pet()));
        verify(petRepository, times(1)).save(any());
        verify(petRepository, times(1)).findById(any());
    }

    @Test
    void savePetShouldThrowEntityAlreadyExistException() {
        //when
        when(petRepository.findById(pet1.getId())).thenReturn(Optional.of(pet1));
        //then
        assertThrows(EntityAlreadyExistException.class, () -> petService.savePet(pet1));
        verify(petRepository, times(0)).save(any());
        verify(petRepository, times(1)).findById(pet1.getId());
    }

    @Test
    void updatePetShouldUpdatePetCorrectly() {
        //when
        when(petRepository.findById(any())).thenReturn(Optional.of(pet1));
        when(petRepository.save(pet1)).thenReturn(pet1);
        //then
        assertEquals(pet1, petService.updatePet(pet1));
        verify(petRepository, times(1)).save(any());
        verify(petRepository, times(1)).findById(any());
    }

    @Test
    void updatePetShouldThrowNotFoundEntityException() {
        //when
        when(petRepository.findById(any())).thenReturn(Optional.empty());
        //then
        assertThrows(NotFoundEntityException.class, () ->
                petService.updatePet(pet1));
        verify(petRepository, times(0)).save(any());
        verify(petRepository, times(1)).findById(any());
    }

    @Test
    void deletePetShouldCorrectlyDeletePet() {
        //when
        when(petRepository.findById(any())).thenReturn(Optional.of(pet1));
        //then
        petService.deletePet(pet1);
        verify(petRepository, times(1)).delete(any());
    }

    @Test
    void deletePetShouldThrowNotFoundEntityException() {
        Pet temp = new Pet();
        temp.setId(3L);
        //when
        when(petRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> petService.deletePet(temp));
    }

}
