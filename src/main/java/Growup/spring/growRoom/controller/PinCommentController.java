package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.PinCommentDtoReq;
import Growup.spring.growRoom.dto.PinCommentDtoRes;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.service.PinCommentService;
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

    /**
     * 24.02.13 작성자 : 류기현
     * 대댓글 조회
     */
    @GetMapping("/{growRoomId}/{pinId}")
    public ApiResponse<List<PinCommentDtoRes.PinCommentViewDtoRes>> findAllPinComments(@PathVariable Long growRoomId, @PathVariable Long pinId){
        List<PinCommentDtoRes.PinCommentViewDtoRes> pinList = pinCommentService.findAllByPinId(pinId)
                .stream()
                .map(PinCommentDtoRes.PinCommentViewDtoRes::new)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.13 작성자 : 류기현
     * Pin{id}의 대댓글(PinComment) 생성
     */
    @PostMapping("/{growRoomId}/{pinId}")
    public ApiResponse<List<PinCommentDtoRes.PinCommentViewDtoRes>> addPinComment(@PathVariable Long growRoomId, @PathVariable Long pinId, @RequestBody PinCommentDtoReq.AddPinCommentDtoReq request){
        pinCommentService.save(pinId, request);
        List<PinCommentDtoRes.PinCommentViewDtoRes> pinList = pinCommentService.findAllByPinId(pinId)
                .stream()
                .map(PinCommentDtoRes.PinCommentViewDtoRes::new)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(pinList);
    }

    /**
     * 24.02.13 작성자 : 류기현
     * Pin{id} 대댓글 수정
     */
    @PutMapping("/{growRoomId}/{pinId}/{pinCommentId}")
    public ApiResponse<PinCommentDtoRes.PinCommentViewDtoRes> updatePin(@PathVariable Long growRoomId, @PathVariable Long pinId, @PathVariable Long pinCommentId, @RequestBody PinCommentDtoReq.UpdatePinCommentDtoReq request) {
        PinComment updatedPinComment = pinCommentService.update(pinId, pinCommentId, request);

        return ApiResponse.onSuccess(new PinCommentDtoRes.PinCommentViewDtoRes(updatedPinComment));
    }

    /**
     * 24.02.13 작성자 : 류기현
     * Pin{id} 대댓글 삭제 - 수정을 통해 상태변경 후 일정 시간 이후 삭제
     */
    @DeleteMapping("/{growRoomId}/{pinId}/{pinCommentId}")
    public ApiResponse<SuccessStatus> deletePin(@PathVariable Long growRoomId, @PathVariable Long pinId, @PathVariable Long pinCommentId){
        pinCommentService.delete(pinCommentId);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
