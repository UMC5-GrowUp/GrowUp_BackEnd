package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PinCommentRepository extends JpaRepository<PinComment, Long> {
    List<PinComment> findAllByPinAndStatusNot(Pin pin, String status);

    List<PinComment> findAllByStatusAndUpdatedAtBefore(String status, LocalDateTime updatedAt);
}
