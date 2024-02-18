package Growup.spring.participate.repository;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    Page<Participate> findAllByGrowRoomIdOrderByUser_nickNameAsc(Long growRoomId , PageRequest pageRequest);
    Page<Participate> findAllByGrowRoomIdOrderByCreatedAtDesc (Long growRoomId , PageRequest pageRequest);
    Page<Participate> findAllByGrowRoomIdOrderByLikedAsc(Long growRoomId , PageRequest pageRequest);
    //Page<Participate> findAllByGrowRoomIdOrderByParticipateTimeAsc(Long growRoomId , PageRequest pageRequest);
    boolean existsById(Long growRoomId);

    //그로우룸 관련
    Participate findByUserIdAndGrowRoomId (Long userId, Long growRoomId);

    List<Participate> findAllByUserId(Long userId);

    Participate findByGrowRoomId(Long growRoomId);
}
