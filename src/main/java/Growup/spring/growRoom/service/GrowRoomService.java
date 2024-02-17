package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GrowRoomService {
    List<GrowRoom> findByFilter(String filter, String categoryDetail, String period, String status, Long userId, String search);

    List<GrowRoom> findHot();

    GrowRoom save(Long userID, GrowRoomDtoReq.AddGrowRoomDtoReq request);

    GrowRoom findById(Long id);

    void deleteTemp(Long userID, Long id);

    GrowRoom update(Long userID, Long id, GrowRoomDtoReq.UpdateGrowRoomDtoReq request);

    int updateView(Long userID, Long id);

    Post inquirypost(Long growRoomId);
}