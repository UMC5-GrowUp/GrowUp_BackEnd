package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.component.Period;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface PeriodRepository extends JpaRepository<Period, Long> {
    @Override
    Optional<Period> findById(Long id);
}
