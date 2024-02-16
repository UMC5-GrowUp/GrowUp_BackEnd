package Growup.spring.participate.controller;


import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.service.GrowRoomService;
import Growup.spring.liked.service.LikedService;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.service.ParticipateService;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/participate")
public class ParticipateController {
    private final ParticipateService participateService;
    private final JwtProvider jwtProvider;
    private final GrowRoomService growRoomService;
    private final LikedService likedService;

    /**
     * 24.02.16 작성자 : 정주현
     * 참여자 그로우룸 입장
     */
    @PostMapping("/enter")
    public ApiResponse<ParticipateDtoRes.participateEnterRes> participateEnter(@RequestParam Long growRoomId){
        Long userId = jwtProvider.getUserID();
        return ApiResponse.onSuccess(participateService.participateEnter(userId,growRoomId));

    }


    /**
     * 24.02.16 작성자 : 정주현
     * 참여자 그로우룸 나가기
     */
    @PatchMapping("/out")
    public ApiResponse<SuccessStatus> participateOut(@RequestParam Long growRoomId){
        Long userId = jwtProvider.getUserID();
        participateService.participateOut(userId,growRoomId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    /**
     * 24.02.17 작성자 : 류기현
     * 라이브룸 목록 조회
     */
    @GetMapping("/under")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findAllGrowRooms(@RequestParam(name = "filter", defaultValue = "전체") String filter){
        Long userId = jwtProvider.getUserID();
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findByFilter(filter, "전체", "전체", "전체", userId, "")
                .stream()
                .map(growRoom -> new GrowRoomDtoRes.GrowRoomAllDtoRes(growRoom, likedService.isGrowRoomLikedByUser(userId, growRoom.getId())))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }

    /*
    //순서별로 조회
    @GetMapping("/orderBy")
    //filter를 사용 하여 구분 지음 filter를 사용 하여 각각의 순서대로 조회 되게 설정 , 기본값(null)으로 날짜순
    public ApiResponse <ParticipateDtoRes.orderByAsc> orderBy(@RequestParam(name = "filter",defaultValue = "createdate") String filter ,
                                                              @RequestParam(name = "growRoomId" ) Long growRoomId ,
                                                              @RequestParam(name = "page") Integer page) {

        Page<Participate> participateList = participateService.LiveupParticipateList(filter, growRoomId, page);

        return ApiResponse.onSuccess(ParticipateConverter.orderByAscDto(participateList.getContent())); //getcontent 를 통해 리스트로 변환
    }

     */




}
