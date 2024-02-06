package Growup.spring.participate.repository;

import Growup.spring.participate.model.Participate;
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
    Page<Participate> findAllByGrowRoomIdOrderByParticipateTimeAsc(Long growRoomId , PageRequest pageRequest);
    boolean existsById(Long growRoomId);

    //그로우룸 관련
    List<Participate> findByUserId (Long userId);


}
