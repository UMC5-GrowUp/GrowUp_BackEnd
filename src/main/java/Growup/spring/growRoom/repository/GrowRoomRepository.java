package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.GrowRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface GrowRoomRepository extends JpaRepository<GrowRoom, Long> {
    @Override
    Optional<GrowRoom> findById(Long id);

    @Modifying
    @Query("update GrowRoom growroom set growroom.view = growroom.view + 1 where growroom.id = :id")
    int updateView(Long id);

    @Modifying
    @Query("update GrowRoom g set g.view = g.view + 1 where g.id = :growRoomId")
    int increaseViews (@Param("growRoomId")Long growRoomId);

    //전체
    Page<GrowRoom> findAllBy (PageRequest pageRequest);

    //내모집글
    Page<GrowRoom> findAllByUserId(Long userId, PageRequest pageRequest);


    //ID 목록에 해당하는 엔티티들을 조회하는 메서드 //WHERE 절 내에서 특정값 여러개를 선택하는 SQL 연산자 // 여기서는 growRoomId여러개를 통해 growRoom 객체를 가져옴
    Page<GrowRoom> findAllByIdIn(List<Long> growRoomId, PageRequest pageRequest);

}
