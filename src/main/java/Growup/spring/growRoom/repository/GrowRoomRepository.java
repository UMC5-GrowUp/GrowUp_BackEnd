package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.Period;
import Growup.spring.participate.model.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {

    List<GrowRoom> findAllByStatusNot(String status);

    @Override
    Optional<GrowRoom> findById(Long id);

    @Modifying
    @Query("update GrowRoom growroom set growroom.view = growroom.view + 1 where growroom.id = :id")
    int updateView(Long id);

    //내 모집글
    List<GrowRoom> findAllByUserId(Long userId);

    //ID 목록에 해당하는 엔티티들을 조회하는 메서드 //WHERE 절 내에서 특정값 여러개를 선택하는 SQL 연산자 // 여기서는 growRoomId여러개를 통해 growRoom 객체를 가져옴
    List<GrowRoom> findAllByIdIn(List<Long> growRoomId);

    List<GrowRoom> findAllByRecruitmentId(Long recruitmentId);

    // 일정시간 이후 삭제
    List<GrowRoom> findAllByStatusAndUpdatedAtBefore(String status, LocalDateTime updateAt);

    // 진행기간별 검색
    List<GrowRoom> findAllByPeriod(Period period);

    // 모집중만 검색
    List<GrowRoom> findAllByStatus(String status);

    GrowRoom findByPost(Post post);
}
