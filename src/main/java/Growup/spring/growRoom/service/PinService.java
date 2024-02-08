package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.PinDtoReq;
import Growup.spring.growRoom.model.Pin;

import java.util.List;

public interface PinService {
    List<Pin> findAllByGrowRoomId(Long id);

    Pin save(Long id, PinDtoReq.AddPinDtoReq request);

    Pin update(Long id, PinDtoReq.UpdatePinDtoReq request);

    void delete(Long id);
}
