package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.controller.PinCommentController;
import Growup.spring.growRoom.converter.PinCommentConverter;
import Growup.spring.growRoom.dto.PinCommentDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
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
    private final JwtProvider jwtProvider;
    private final PinCommentConverter pinCommentConverter;

    @Override
    public List<PinComment> findAllByPinId(Long pinId) {
        return pinCommentRepository.findAllByPin(pinRepository.findById(pinId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND)));
    }

    @Override
    public List<PinComment> save(Long pinId, PinCommentDtoReq.AddPinCommentDtoReq request) {
        User user = userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Pin pin = pinRepository.findById(pinId)
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND));

        PinComment pinComment = pinCommentConverter.convertToPinComment(pin, user, request.getComment());
        pinCommentRepository.save(pinComment);

        return pinCommentRepository.findAllByPin(pinRepository.findById(pinId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND)));
    }

    @Transactional
    @Override
    public PinComment update(Long pinId, Long pinCommentId, PinCommentDtoReq.UpdatePinCommentDtoReq request) {
        PinComment pinComment = pinCommentRepository.findById(pinCommentId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PINCOMMENT_NOT_FOUND));
        pinComment.update(request.getComment());

        return pinComment;
    }

    @Transactional
    @Override
    public void delete(Long pinCommentId) {
        PinComment pinComment = pinCommentRepository.findById(pinCommentId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PINCOMMENT_NOT_FOUND));
        pinComment.setStatus("1");
    }
}
