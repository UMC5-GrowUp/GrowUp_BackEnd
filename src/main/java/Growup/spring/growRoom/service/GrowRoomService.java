package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GrowRoomService {
    List<GrowRoom> findAll();

    GrowRoom save(GrowRoomDtoReq.AddGrowRoomDtoReq request);

    GrowRoom findById(Long id);

    void deleteTemp(Long id);

    GrowRoom update(Long id, GrowRoomDtoReq.UpdateGrowRoomDtoReq request);

    int updateView(Long id);

    int viewincrease (Long growRoomId);

    Post inquirypost(Long growRoomId);

    Page<GrowRoom> GrowRoomList (String filter , Long userId , Integer page );
}