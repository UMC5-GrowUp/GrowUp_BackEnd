package Growup.spring.user.repository;

import Growup.spring.user.model.Enum.UserState;
import Growup.spring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long usePk);

    Optional<User> findByEmailAndStatus(String email,UserState status);

    boolean existsByEmailAndStatus(String email, UserState status);


    boolean existsByNickName(String NickName);

}

