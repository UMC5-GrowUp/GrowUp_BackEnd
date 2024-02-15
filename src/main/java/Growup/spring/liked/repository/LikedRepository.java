package Growup.spring.liked.repository;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.liked.model.Liked;
import Growup.spring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface LikedRepository extends JpaRepository<Liked, Long> {

    Optional<Liked> findByUserAndGrowRoom(User user , GrowRoom growRoom); // user와 growRoom찾는 메서드

    List<Liked> findByGrowRoomId(Long growRoomId); //like리스트를 찾는 메서드

    boolean existsLikedByUserAndGrowRoom(User user, GrowRoom growRoom);

    List<Liked> findByUserId(Long userId);

}
