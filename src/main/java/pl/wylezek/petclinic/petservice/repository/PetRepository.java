package pl.wylezek.petclinic.petservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wylezek.petclinic.petservice.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
