package Growup.spring.calender.dto;

import Growup.spring.calender.model.Enum.CalenderColor;
import Growup.spring.calender.model.Enum.CalenderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CalenderDtoRes {

    //캘린더 등록 응답
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class calenderEnrollRes{
        private Long userId;
        private Long calenderId;
        private LocalDateTime createAt;
    }

    //캘린더 조회 응답 - 하루 기준
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class calenderInquiryResultRes{
        private Long userId;
        private LocalDate day;
        private List<calenderInquiryRes> calenderInquiryLists;
    }

    //캘린더 조회
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class calenderInquiryRes {
        private Long calenderId;
        private String comment;
        private CalenderStatus status;
        private LocalDate day;
    }

    //캘린더 한달 기준 조회
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class calenderMonthInquiryRes{
        private LocalDate day;
        private List<calenderInquiryRes> calenderInquiryLists;
    }


    //캘린더 한달 기준 전체 조회
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class calenderMonthInquiryResultRes{
        private Long userId;
        private List<calenderMonthInquiryRes> calenderMonthInquiryLists;
    }



}
