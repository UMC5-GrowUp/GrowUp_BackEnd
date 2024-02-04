package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;

import java.util.List;

public interface GrowRoomService {
    List<GrowRoom> findAll();

    GrowRoom save(GrowRoomDtoReq.AddGrowRoomDtoReq request);

    GrowRoom findById(Long id);

    void deleteTemp(Long id);

    GrowRoom update(Long id, GrowRoomDtoReq.UpdateGrowRoomDtoReq request);

    int updateView(Long id);
}