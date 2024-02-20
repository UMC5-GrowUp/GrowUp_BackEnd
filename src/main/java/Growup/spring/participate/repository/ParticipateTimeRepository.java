package Growup.spring.participate.repository;

import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateTimeRepository extends JpaRepository<ParticipateTime, Long> {
    ParticipateTime findFirstByParticipateOrderByCreatedAtDesc(Participate participate);

    ParticipateTime findTopByParticipateOrderByCreatedAtDesc(Participate participate);
}
