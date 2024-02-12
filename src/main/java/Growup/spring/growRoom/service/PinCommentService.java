package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.PinCommentDtoReq;
import Growup.spring.growRoom.dto.PinDtoReq;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;

import java.util.List;

public interface PinCommentService {
    List<PinComment> findAllByPinId(Long pinId);

    List<PinComment> save(Long pinId, PinCommentDtoReq.AddPinCommentDtoReq request);

    PinComment update(Long pinId, Long pinCommentId, PinCommentDtoReq.UpdatePinCommentDtoReq request);

    void delete(Long pinCommentId);
}
