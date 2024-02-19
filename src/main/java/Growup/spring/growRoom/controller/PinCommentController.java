package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.PinCommentDtoReq;
import Growup.spring.growRoom.dto.PinCommentDtoRes;
import Growup.spring.growRoom.dto.PinDtoRes;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.service.PinCommentService;
import Growup.spring.growRoom.service.PinService;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
@RequestMapping("/growup/growroom")
public class PinCommentController {

    // 대댓글
    private final PinCommentService pinCommentService;
    private final PinService pinService;
    private final JwtProvider jwtProvider;

    /**
     * 24.02.13 작성자 : 류기현
     * 대댓글 조회
     */
    @GetMapping("/{growRoomId}/{pinId}")
    public ApiResponse<List<PinDtoRes.PinViewDtoRes>> findAllPinComments(@PathVariable Long growRoomId, @PathVariable Long pinId){
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.13 작성자 : 류기현
     * Pin{id}의 대댓글(PinComment) 생성
     */
    @PostMapping("/{growRoomId}/{pinId}")
    public ApiResponse<List<PinDtoRes.PinViewDtoRes>> addPinComment(@PathVariable Long growRoomId, @PathVariable Long pinId, @RequestBody PinCommentDtoReq.AddPinCommentDtoReq request){
        Long userId = jwtProvider.getUserID();
        pinCommentService.save(userId, pinId, request);
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.13 작성자 : 류기현
     * Pin{id} 대댓글 수정
     */
    @PutMapping("/{growRoomId}/{pinId}/{pinCommentId}")
    public ApiResponse<List<PinDtoRes.PinViewDtoRes>> updatePin(@PathVariable Long growRoomId, @PathVariable Long pinId, @PathVariable Long pinCommentId, @RequestBody PinCommentDtoReq.UpdatePinCommentDtoReq request) {
        Long userId = jwtProvider.getUserID();
        pinCommentService.update(userId, pinId, pinCommentId, request);
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.13 작성자 : 류기현
     * Pin{id} 대댓글 삭제 - 수정을 통해 상태변경 후 일정 시간 이후 삭제
     */
    @DeleteMapping("/{growRoomId}/{pinId}/{pinCommentId}")
    public ApiResponse<List<PinDtoRes.PinViewDtoRes>> deletePin(@PathVariable Long growRoomId, @PathVariable Long pinId, @PathVariable Long pinCommentId){
        Long userId = jwtProvider.getUserID();
        pinCommentService.delete(userId, pinCommentId);
        List<PinDtoRes.PinViewDtoRes> pinList = pinService.pinRes(growRoomId);

        return ApiResponse.onSuccess(pinList);
    }
}
