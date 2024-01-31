package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.component.Number;
import Growup.spring.growRoom.model.component.Period;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface NumberRepository extends JpaRepository<Number, Long> {
    @Override
    Optional<Number> findById(Long id);
}
