package Growup.spring.calender.repository;

import Growup.spring.calender.model.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calender,Long> {
}
