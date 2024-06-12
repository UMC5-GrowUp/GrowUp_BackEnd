package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findAllByGrowRoomIdAndStatusNot(Long growRoomId, String status);

    List<Pin> findAllByStatusAndUpdatedAtBefore(String status, LocalDateTime updatedAt);
}
