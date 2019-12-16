package pl.wylezek.petclinic.petservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.wylezek.petclinic.petservice.exceptions.custom.EmptyEntityListException;
import pl.wylezek.petclinic.petservice.exceptions.custom.EntityAlreadyExistException;
import pl.wylezek.petclinic.petservice.exceptions.custom.NotFoundEntityException;
import pl.wylezek.petclinic.petservice.model.Pet;
import pl.wylezek.petclinic.petservice.service.PetService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper object;

    @MockBean
    private PetService petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = new Pet(1L, "Thor", 4,null);
        pet2 = new Pet(2L, "Arthur", 12, null);
    }

    @Test
    void getPetByIdShouldReturnCorrectlyPet() throws Exception {
        //when
        when(petService.getPetById(anyLong())).thenReturn(pet1);
        //then
        mockMvc.perform(get("/api/v1/pets/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(object.writeValueAsString(pet1)));
    }

    @Test
    void getPetByIdShouldReturnNotFound() throws Exception {
        //when
        when(petService.getPetById(anyLong())).thenThrow(new NotFoundEntityException(Pet.class));
        //then
        mockMvc.perform(get("/api/v1/pets/{id}", 33L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getPetsByCategoryShouldReturnCorrectlyListOfPets() throws Exception {
        //when
        when(petService.getPetsByCategory(anyString())).thenReturn(List.of(pet1, pet2));
        //then
        mockMvc.perform(get("/api/v1/pets")
                .param("category", "Dogs"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(object.writeValueAsString(List.of(pet1, pet2))));
    }

    @Test
    void getPetsByCategoryShouldReturnNotFound() throws Exception {
        //when
        when(petService.getPetsByCategory(anyString())).thenThrow(new EmptyEntityListException(Pet.class));
        //then
        mockMvc.perform(get("/api/v1/pets")
                .param("category", "Dogs"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnBadRequestForGetMethod() throws Exception {
        //then
        mockMvc.perform(get("/api/v1/pets"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void addNewPetShouldCreateCorrectlyNewPet() throws Exception {
        //when
        when(petService.savePet(any(Pet.class))).thenReturn(pet1);
        //then
        mockMvc.perform(post("/api/v1/pets")
                .content(object.writeValueAsString(pet1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(object.writeValueAsString(pet1)))
                .andDo(print());
    }

    @Test
    void addNewPetShouldReturnConflict() throws Exception {
        //when
        when(petService.savePet(pet1)).thenThrow(new EntityAlreadyExistException(Pet.class));
        //then
        mockMvc.perform(post("/api/v1/pets")
                .content(object.writeValueAsString(pet1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void shouldReturnBadRequestForPostMethod() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/pets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void shouldReturnBadRequestForPutMethod() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/pets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void shouldReturnBadRequestForDeleteMethod() throws Exception {
        //then
        mockMvc.perform(delete("/api/v1/pets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeForPostMethod() throws Exception {
        //then
        mockMvc.perform(post("/api/v1/pets")
                .content(object.writeValueAsString(pet1))
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeForPutMethod() throws Exception {
        //then
        mockMvc.perform(put("/api/v1/pets")
                .content(object.writeValueAsString(pet1))
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void updatePetShouldUpdatePetCorrectly() throws Exception {
        //when
        when(petService.updatePet(any())).thenReturn(pet1);
        //then
        mockMvc.perform(put("/api/v1/pets")
                .content(object.writeValueAsString(pet1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(object.writeValueAsString(pet1)))
                .andDo(print());
    }

    @Test
    void updatePetShouldReturnNotFound() throws Exception {
        //when
        when(petService.updatePet(any())).thenThrow(new NotFoundEntityException(Pet.class));
        //then
        mockMvc.perform(put("/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object.writeValueAsString(pet1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void deletePetCorrectlyDeletePet() throws Exception {
        //when
        doNothing().when(petService).deletePet(any());
        //then
        mockMvc.perform(delete("/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(object.writeValueAsString(pet1)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deletePetShouldReturnNotFound() throws Exception {
        //when
        Mockito.doThrow(NotFoundEntityException.class).when(petService).deletePet(any());
        //then
        mockMvc.perform(delete("/api/v1/pets")
                .content(object.writeValueAsString(pet1))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
