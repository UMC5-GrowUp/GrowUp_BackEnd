package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.component.GrowRoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrowRoomCategoryRepository extends JpaRepository<GrowRoomCategory, Long> {
    @Override
    Optional<GrowRoomCategory> findById(Long id);
}
