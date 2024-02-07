package Growup.spring.calender.repository;

import Growup.spring.calender.model.Calender;
import Growup.spring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalenderRepository extends JpaRepository<Calender,Long> {

    List<Calender> findByUser(User user);

    Optional<Calender> findById(Long CalenderId);


}
