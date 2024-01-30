package Growup.spring.growRoom.repository;



import Growup.spring.growRoom.model.GrowRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {

}
