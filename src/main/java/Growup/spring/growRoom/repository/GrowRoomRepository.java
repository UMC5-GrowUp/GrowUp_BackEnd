package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.GrowRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {
    @Override
    Optional<GrowRoom> findById(Long id);
}
