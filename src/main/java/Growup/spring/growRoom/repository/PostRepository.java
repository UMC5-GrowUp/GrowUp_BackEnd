package Growup.spring.growRoom.repository;

import Growup.spring.growRoom.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Optional<Post> findById(Long id);

    Post findByGrowRoomId(Long growRoomId);

    // title을 통한 검색
    List<Post> findAllByTitleContaining(String search);
}
