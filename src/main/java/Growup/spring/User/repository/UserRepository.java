package Growup.spring.User.repository;

import Growup.spring.User.model.Enum.UserState;
import Growup.spring.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long usePk);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndStatus(String email,UserState status);

    boolean existsByEmailAndStatus(String email, UserState status);

    boolean existsByEmail(String email);


    boolean existsByNickName(String NickName);

}

