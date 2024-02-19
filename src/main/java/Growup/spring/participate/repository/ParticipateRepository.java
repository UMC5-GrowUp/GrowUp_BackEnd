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


    //그로우룸(참여) 관련
    Participate findByUserIdAndGrowRoomId (Long userId, Long growRoomId);

    List<Participate> findByGrowRoomId(Long growroomId);

    List<Participate> findByGrowRoomIdOrderByUser_NameAsc(Long growRoomId);

    List<Participate> findByGrowRoomIdAndLiked(Long growRoomId,int like);


    List<Participate> findAllByUserId(Long userId);
}
