package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.component.Period;
import Growup.spring.growRoom.model.component.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    @Override
    Optional<Recruitment> findById(Long id);
}
