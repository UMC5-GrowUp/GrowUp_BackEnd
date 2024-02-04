package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.GrowRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {
    @Override
    Optional<GrowRoom> findById(Long id);

    @Modifying
    @Query("update GrowRoom growroom set growroom.view = growroom.view + 1 where growroom.id = :id")
    int updateView(Long id);
}
