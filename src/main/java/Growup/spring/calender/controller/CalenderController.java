package Growup.spring.calender.controller;

import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.model.Calender;
import Growup.spring.calender.service.CalenderService;
import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/growup/calender")
public class CalenderController {
    private final JwtProvider jwtProvider;
    private final CalenderService calenderService;


    //목록 (달기준) 조회
    @GetMapping("/inquiry-month")
    public ApiResponse<CalenderDtoRes.calenderMonthInquiryResultRes> calenderMonthInquiry(@RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth day) {
        Long userId = jwtProvider.getUserID();
        return ApiResponse.onSuccess(calenderService.calenderMonthInquiry(userId, day));
    }


    //해당 날짜(일) 조회
    @GetMapping("/inquiry")
    public ApiResponse<CalenderDtoRes.calenderInquiryResultRes> calenderInquiry(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day){
        Long userId = jwtProvider.getUserID();
        return ApiResponse.onSuccess(calenderService.calenderInquiry(day,userId));
    }

    //목록 등록
    @PostMapping("/enroll")
    public ApiResponse<CalenderDtoRes.calenderEnrollRes> calenderEnroll(@RequestBody @Valid CalenderDtoReq.calenderEnroll request){
        Long userId = jwtProvider.getUserID();
        Calender calender = calenderService.calenderEnroll(userId,request);


        return ApiResponse.onSuccess(CalenderConverter.calenderEnrollRes(userId,calender));
    }

    //목록 수정(글)
    @PatchMapping("/comment-modify")
    public ApiResponse<SuccessStatus> calenderCommentModify(@RequestBody @Valid CalenderDtoReq.calenderCommentModify request){
        Long userId = jwtProvider.getUserID();
        calenderService.calenderCommentModify(userId,request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

    //목록 상태 수정(줄 긋기)
    @PatchMapping("/status-modify")
    public ApiResponse<SuccessStatus> calenderStatusModify(@RequestParam Long calenderId){
        calenderService.calenderStatusModify(calenderId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

/*
    //목록 색깔 수정
    @PatchMapping("color-modify")
    public ApiResponse<SuccessStatus> calenderColorModify(@RequestParam Long calenderId){
         calenderService.calenderColorModify(calenderId);
         return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }

 */

    //목록 삭제
    @DeleteMapping("/delete")
    public ApiResponse<SuccessStatus> calenderDelete(@RequestParam Long calenderId){
        calenderService.calenderDelete(calenderId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }





}
