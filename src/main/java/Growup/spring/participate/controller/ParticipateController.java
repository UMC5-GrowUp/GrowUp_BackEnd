package Growup.spring.participate.controller;


import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.service.GrowRoomService;
import Growup.spring.liked.service.LikedService;
import Growup.spring.participate.converter.ParticipateConverter;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.service.ParticipateService;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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
     * 24.02.18 작성자 : 정주현
     * 라이브룸 - 참여자 목록 조회
     */

    @GetMapping("/inquiry")
    public ApiResponse<ParticipateDtoRes.participateInquiryList> participateInquiry(@RequestParam(name = "filter", defaultValue = "전체순") String filter
            ,@RequestParam Long growRoomId){
        Long userId = jwtProvider.getUserID();
        return ApiResponse.onSuccess(participateService.participateInquiry(userId,growRoomId,filter));
    }

    /**
     * 24.02.19 작성자 : 정주현
     * 해당 방의 전 날 누적 시간 - 모든 참여자
     */
    @GetMapping("inquiry-yesterdayTime")
    public ApiResponse<ParticipateDtoRes.liveRoomYesterdayTime> liveRoomYesterdayTime(@RequestParam Long growRoomId){
        return ApiResponse.onSuccess(participateService.liveRoomYesterdayTimeInquiry(growRoomId));
    }

    /**
     * 24.02.19 작성자 : 정주현
     * 총 누적시간 랭킹 - 일/주/월 별로
     */
    @GetMapping("inquiry-DateTime")
    public ApiResponse<ParticipateDtoRes.liveRoomDateTimeList> liveRoomDateTimeRank(@RequestParam(name = "filter", defaultValue = "일간") String filter){
        List<ParticipateDtoRes.liveRoomDateTimeRes> list = participateService.liveRoomDateTimeRank(filter);
        return ApiResponse.onSuccess(ParticipateConverter.liveRoomDateTimeResList(list));
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

    /**
     * 24.02.17 작성자 : 류기현
     * 라이브룸 방장 조회
     */
    @GetMapping("/madeBy")
    public ApiResponse<ParticipateDtoRes.ownerRes> findOwner(@RequestParam(name = "growRoomId", defaultValue = "") Long growRoomId){

        return ApiResponse.onSuccess(new  ParticipateDtoRes.ownerRes(participateService.findOwner(growRoomId)));
    }




}
