package Growup.spring.growRoom.service;

import Growup.spring.growRoom.dto.PinCommentDtoReq;
import Growup.spring.growRoom.dto.PinCommentDtoRes;
import Growup.spring.growRoom.model.PinComment;

import java.util.List;

public interface PinCommentService {
    List<PinComment> findAllByPinId(Long pinId);

    List<PinComment> save(Long userId, Long pinId, PinCommentDtoReq.AddPinCommentDtoReq request);

    PinComment update(Long userId, Long pinId, Long pinCommentId, PinCommentDtoReq.UpdatePinCommentDtoReq request);

    void delete(Long userId, Long pinCommentId);

    List<PinCommentDtoRes.PinCommentViewDtoRes> res(List<PinComment> pinComments);
}
