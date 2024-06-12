package Growup.spring.participate.repository;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.participate.model.Enum.ParticipateStatus;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface ParticipateRepository extends JpaRepository<Participate, Long> {


    //그로우룸(참여) 관련
    Participate findByUserIdAndGrowRoomIdAndStatus (Long userId, Long growRoomId,ParticipateStatus status);

    List<Participate> findByGrowRoomIdAndStatus(Long growroomId, ParticipateStatus status);

    List<Participate> findByGrowRoomId(Long growroomId);

    List<Participate> findByGrowRoomIdAndStatusOrderByUser_NameAsc(Long growRoomId,ParticipateStatus status);

    List<Participate> findByGrowRoomIdAndStatusAndLiked(Long growRoomId,ParticipateStatus status,int like);

    Participate findByUserIdAndGrowRoomId(Long userId, Long growRoomId);

    List<Participate> findAllByUserId(Long userId);
}
