package Growup.spring.participate.service;

import Growup.spring.participate.model.Participate;
import org.springframework.data.domain.Page;

public interface ParticipateService {
    public Page<Participate> LiveupParticipateList (String filter , Long growRoomId , Integer page );
}
