package Growup.spring.participate.service;

import Growup.spring.participate.model.Participate;

import java.util.List;

public interface ParticipateService {
    public List <Participate> LiveupParticipateList (String filter , Long growRoomId);
}
