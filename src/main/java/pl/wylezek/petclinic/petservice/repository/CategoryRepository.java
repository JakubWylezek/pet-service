package pl.wylezek.petclinic.petservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wylezek.petclinic.petservice.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
