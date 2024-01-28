package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.domain.GrowRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {
}
