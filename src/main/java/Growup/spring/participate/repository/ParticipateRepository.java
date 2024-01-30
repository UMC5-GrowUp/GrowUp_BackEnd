package Growup.spring.participate.repository;

import Growup.spring.participate.model.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    List<Participate> findAllByGrowRoomIdOrderByUser_nickNameAsc(Long growRoomId);
    List<Participate> findAllByGrowRoomIdOrderByCreatedAtDesc (Long growRoomId);
    List<Participate> findAllByGrowRoomIdOrderByLikedAsc(Long growRoomId);

    List<Participate> findAllByGrowRoomIdOrderByParticipateTimeAsc(Long growRoomId);
    List findAllByGrowRoomId(Long growRoomId);

}
