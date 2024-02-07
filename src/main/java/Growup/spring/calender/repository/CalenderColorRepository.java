package Growup.spring.calender.repository;

import Growup.spring.calender.model.Calender;
import Growup.spring.calender.model.CalenderColor;
import Growup.spring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalenderColorRepository extends JpaRepository<CalenderColor,Long> {

    CalenderColor findByUserAndDay(User user, LocalDate day);

    List<CalenderColor> findByUserAndDayBetween(User user,LocalDate startOfMonth,LocalDate endOfMonth);

}
