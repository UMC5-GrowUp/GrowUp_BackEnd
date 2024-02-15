package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GrowRoomService {
    List<GrowRoom> findByFilter(String filter, String category, String period, String recruit, Long userId, String search);

    GrowRoom save(GrowRoomDtoReq.AddGrowRoomDtoReq request);

    GrowRoom findById(Long id);

    void deleteTemp(Long id);

    GrowRoom update(Long id, GrowRoomDtoReq.UpdateGrowRoomDtoReq request);

    int updateView(Long id);

    Post inquirypost(Long growRoomId);
}