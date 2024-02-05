package Growup.spring.calender.converter;

import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.model.Calender;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalenderConverter {

    //캘린더 객체 생성
    public static Calender toCcalender(User user, CalenderDtoReq.calenderEnroll request){
        return Calender.builder()
                .user(user)
                .day(request.getDay())
                .comment(request.getComment())
                .build();
    }

    //캘린더 목록 등록 응답
    public static CalenderDtoRes.calenderEnrollRes calenderEnrollRes(Long userId, Calender calender){
        return CalenderDtoRes.calenderEnrollRes.builder()
                .userId(userId)
                .calenderId(calender.getId())
                .createAt(calender.createdAt)
                .build();
    }

    //캘린더 목록 조회
    public static CalenderDtoRes.calenderInquiryRes calenderInquiryRes(Calender calender){
        return CalenderDtoRes.calenderInquiryRes.builder()
                .calenderId(calender.getId())
                .comment(calender.getComment())
                .status(calender.getStatus())
                .day(calender.getDay())
                .build();
    }

    //캘린더 특정 날짜 조회 응답
    public static CalenderDtoRes.calenderInquiryResultRes calenderInquiryResultRes(Long userId, LocalDate day, List<CalenderDtoRes.calenderInquiryRes> calenderInquiryRes){
        return CalenderDtoRes.calenderInquiryResultRes.builder()
                .userId(userId)
                .day(day)
                .calenderInquiryLists(calenderInquiryRes)
                .build();
    }

    //캘린더 특정 달 조회 - 특정 날짜 리스트화+날짜
    public static CalenderDtoRes.calenderMonthInquiryRes calenderMonthInquiryRes(LocalDate date, Map<LocalDate, List<CalenderDtoRes.calenderInquiryRes>> groupedByDay){
        return CalenderDtoRes.calenderMonthInquiryRes.builder()
                .day(date)
                .calenderInquiryLists(groupedByDay.getOrDefault(date, List.of()))
                .build();
    }

    //캘린더 특정 달 조회 총 결과
    public static CalenderDtoRes.calenderMonthInquiryResultRes calenderMonthInquiryResultRes(Long userId,List<CalenderDtoRes.calenderMonthInquiryRes> calenderMonthInquiryResList) {
        return CalenderDtoRes.calenderMonthInquiryResultRes.builder()
                .userId(userId)
                .calenderMonthInquiryLists(calenderMonthInquiryResList)
                .build();
    }


}
