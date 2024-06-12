package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.converter.PinConverter;
import Growup.spring.growRoom.dto.PinDtoReq;
import Growup.spring.growRoom.dto.PinDtoRes;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.service.PinService;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
@RequestMapping("/growup/growroom")
public class PinController {

    // 댓글 Pin
    private final PinService pinService;
    private final JwtProvider jwtProvider;

    /**
     * 24.02.06 작성자 : 류기현
     * 그로우룸 {id} 댓글 조회 + 대댓글 조회로 만들어야함
     */
    @GetMapping("/{growRoomId}/pin")
    public ApiResponse<List<PinDtoRes.PinViewDtoRes>> findAllPins(@PathVariable Long growRoomId){
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.07 작성자 : 류기현
     * 그로우룸 {id} 댓글 생성
     */
    @PostMapping("/{growRoomId}")
    public ApiResponse<List<PinDtoRes.PinViewDtoRes>> addPin(@PathVariable Long growRoomId, @RequestBody PinDtoReq.AddPinDtoReq request){
        Long userId = jwtProvider.getUserID();
        pinService.save(userId, growRoomId, request);
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.07 작성자 : 류기현
     * 그로우룸 {id} 댓글 수정
     */
    @PutMapping("/{growRoomId}/{pinId}")
    public ApiResponse<List <PinDtoRes.PinViewDtoRes>> updatePin(@PathVariable Long growRoomId, @PathVariable long pinId, @RequestBody PinDtoReq.UpdatePinDtoReq request) {
        Long userId = jwtProvider.getUserID();
        pinService.update(userId, pinId, request);
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.04 작성자 : 류기현
     * 그로우룸 {id} 삭제 - 수정을 통해 상태변경 후 일정 시간 이후 삭제
     */
    @DeleteMapping("/{growRoomId}/{pinId}")
    public ApiResponse<List <PinDtoRes.PinViewDtoRes>> deletePin(@PathVariable Long growRoomId, @PathVariable Long pinId){
        Long userId = jwtProvider.getUserID();
        pinService.delete(userId, pinId);
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }
}
