package Growup.spring.calender.service;

import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.model.Calender;

import java.time.LocalDate;
import java.time.YearMonth;

public interface CalenderService {
    CalenderDtoRes.calenderInquiryResultRes calenderInquiry(LocalDate day, Long userId);

    Calender calenderEnroll(Long userId, CalenderDtoReq.calenderEnroll request);

    CalenderDtoRes.calenderMonthInquiryResultRes calenderMonthInquiry(Long userId, YearMonth day);

    void calenderCommentModify(Long userId, CalenderDtoReq.calenderCommentModify request);

    void calenderStatusModify(Long calenderId);

    void calenderDelete(Long calenderId);

    void calenderColorModify(Long userId,CalenderDtoReq.calenderColorModify request);
}
