package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinCommentRepository extends JpaRepository<PinComment, Long> {
    List<PinComment> findAllByPin(Pin pin);
}
