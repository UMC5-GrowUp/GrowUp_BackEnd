package Growup.spring.calender.controller;

import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderRes;
import Growup.spring.calender.model.Calender;
import Growup.spring.calender.service.CalenderService;
import Growup.spring.constant.ApiResponse;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/calender")
public class CalenderController {
    private final JwtProvider jwtProvider;
    private final CalenderService calenderService;

    //목록 (달기준) 조회

    //해당 날짜 조회

    //목록 등록
    @PostMapping("/enroll")
    public ApiResponse<CalenderRes.calenderEnrollRes> calenderEnroll(@RequestBody @Valid CalenderDtoReq.calenderEnroll request){
        Long userId = jwtProvider.getUserID();
        Calender calender = calenderService.calenderEnroll(userId,request);

        return ApiResponse.onSuccess(CalenderConverter.calenderEnrollRes(userId,calender));
    }

    //목록 수정(글)

    //목록 상태 수정(줄 긋기)

    //목록 색깔 수정

    //목록 삭제





}
