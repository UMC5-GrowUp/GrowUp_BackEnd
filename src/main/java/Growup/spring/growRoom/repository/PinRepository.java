package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {
    List<Pin> findAllByGrowRoomId(Long growRoomId);
}
