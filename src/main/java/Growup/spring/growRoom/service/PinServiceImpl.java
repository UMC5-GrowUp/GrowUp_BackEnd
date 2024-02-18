package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.converter.PinCommentConverter;
import Growup.spring.growRoom.converter.PinConverter;
import Growup.spring.growRoom.dto.PinDtoReq;
import Growup.spring.growRoom.dto.PinDtoRes;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.growRoom.repository.PinCommentRepository;
import Growup.spring.growRoom.repository.PinRepository;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PinServiceImpl implements PinService {

    private final PinRepository pinRepository;
    private final PinConverter pinConverter;
    private final GrowRoomRepository growRoomRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PinCommentService pinCommentService;

    @Override
    public List<Pin> findAllByGrowRoomId(Long growRoomId) {
        // 삭제(status=1)되지 않은 Pin만 조회
        return pinRepository.findAllByGrowRoomIdAndStatusNot(growRoomId, "1");
    }

    @Override
    @Transactional
    public Pin save(Long id,PinDtoReq.AddPinDtoReq request) {
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
        User user = userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Pin pin = pinConverter.convertToPin(growRoom, user, request.getComment());
        pinRepository.save(pin);

        return pin;
//        return pinConverter.convertToPin(growRoom, user, request.getComment());
    }

    @Override
    @Transactional
    public Pin update(Long id, PinDtoReq.UpdatePinDtoReq request){
        Pin pin = pinRepository.findById(id).orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND));
        pin.update(request.getComment());

        return pin;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Pin pin = pinRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PIN_NOT_FOUND));
        pin.updateStatus("1");
    }

    @Override
    public List<PinDtoRes.PinViewDtoRes> pinRes(Long growRoomId) {
        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        return convertToPinRes(growRoom);
    }

    public List<PinDtoRes.PinViewDtoRes> convertToPinRes(GrowRoom growRoom){
        List<Pin> pins = findAllByGrowRoomId(growRoom.getId());
        List<PinDtoRes.PinViewDtoRes> pinViewDtoRes = new ArrayList<>();

        for (Pin pin : pins) {
            List<PinComment> pinComments = pinCommentService.findAllByPinId(pin.getId());
            pinViewDtoRes.add(new PinDtoRes.PinViewDtoRes(pin, pinCommentService.res(pinComments)));
        }

        return pinViewDtoRes;
    }
}