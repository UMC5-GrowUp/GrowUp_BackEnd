package Growup.spring.calender.converter;

import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderRes;
import Growup.spring.calender.model.Calender;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public static CalenderRes.calenderEnrollRes calenderEnrollRes(Long userId, Calender calender){
        return CalenderRes.calenderEnrollRes.builder()
                .userId(userId)
                .calenderId(calender.getId())
                .createAt(calender.createdAt)
                .build();

    }


}
