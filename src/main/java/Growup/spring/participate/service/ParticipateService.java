package Growup.spring.participate.service;

import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import org.springframework.data.domain.Page;

public interface ParticipateService {
    ParticipateDtoRes.participateEnterRes participateEnter(Long userId, Long growRoomId);

    void participateOut(Long userId, Long growRoomId);

    Long findOwner(Long growRoomId);

    //Page<Participate> LiveupParticipateList (String filter , Long growRoomId , Integer page );
}
