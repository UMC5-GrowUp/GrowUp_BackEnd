package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.PinDtoReq;
import Growup.spring.growRoom.dto.PinDtoRes;
import Growup.spring.growRoom.model.Pin;

import java.util.List;

public interface PinService {
    List<Pin> findAllByGrowRoomId(Long id);

    Pin save(Long userId, Long id, PinDtoReq.AddPinDtoReq request);

    Pin update(Long userId, Long id, PinDtoReq.UpdatePinDtoReq request);

    void delete(Long userId, Long id);

    List<PinDtoRes.PinViewDtoRes> pinRes(Long growRoomId);
}
