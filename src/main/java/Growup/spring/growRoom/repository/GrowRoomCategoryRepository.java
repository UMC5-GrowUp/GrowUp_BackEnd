package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.component.GrowRoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GrowRoomCategoryRepository extends JpaRepository<GrowRoomCategory, Long> {

    @Override
    Optional<GrowRoomCategory> findById(Long id);

    @Query("DELETE from GrowRoomCategory where growRoom.id =:id")
    @Modifying
    void deleteGrowRoomCategoriesByGrowRoomId(long id);
}
