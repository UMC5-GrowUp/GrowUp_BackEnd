package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.converter.PinCommentConverter;
import Growup.spring.growRoom.dto.PinCommentDtoReq;
import Growup.spring.growRoom.dto.PinCommentDtoRes;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.repository.PinCommentRepository;
import Growup.spring.growRoom.repository.PinRepository;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PinCommentServiceImpl implements PinCommentService {

    private final PinCommentRepository pinCommentRepository;
    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final PinCommentConverter pinCommentConverter;

    @Override
    public List<PinComment> findAllByPinId(Long pinId) {
        return pinCommentRepository.findAllByPinAndStatusNot(pinRepository.findById(pinId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND)), "1");
    }

    @Transactional
    @Override
    public List<PinComment> save(Long userId, Long pinId, PinCommentDtoReq.AddPinCommentDtoReq request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Pin pin = pinRepository.findById(pinId)
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND));

        PinComment pinComment = pinCommentConverter.convertToPinComment(pin, user, request.getComment());
        pinCommentRepository.save(pinComment);

        return pinCommentRepository.findAllByPinAndStatusNot(pinRepository.findById(pinId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND)), "1");
    }

    @Transactional
    @Override
    public PinComment update(Long userId ,Long pinId, Long pinCommentId, PinCommentDtoReq.UpdatePinCommentDtoReq request) {
        PinComment pinComment = pinCommentRepository.findById(pinCommentId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PINCOMMENT_NOT_FOUND));
        // pinComment를 생성한 userId와 수정요청한 userId가 다르다면 error
        if(!userId.equals(pinComment.getUser().getId()))
            throw new UserHandler(ErrorStatus.USER_NOT_PERMITTED);

        pinComment.update(request.getComment());

        return pinComment;
    }

    @Transactional
    @Override
    public void delete(Long userId, Long pinCommentId) {
        PinComment pinComment = pinCommentRepository.findById(pinCommentId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PINCOMMENT_NOT_FOUND));
        // pinComment를 생성한 userId와 수정요청한 userId가 다르다면 error
        if(!userId.equals(pinComment.getUser().getId()))
            throw new UserHandler(ErrorStatus.USER_NOT_PERMITTED);

        pinComment.updateStatus("1");
    }

    @Override
    public List<PinCommentDtoRes.PinCommentViewDtoRes> res(List<PinComment> pinComments) {
        return pinCommentConverter.convertToPinCommentRes(pinComments);
    }
}
