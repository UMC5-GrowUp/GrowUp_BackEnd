package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.component.CategoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {
    @Override
    Optional<CategoryDetail> findById(Long id);
}
