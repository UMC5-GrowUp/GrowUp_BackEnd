package Growup.spring.calender.service;

import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.model.Calender;

public interface CalenderService {
    Calender calenderEnroll(Long userId, CalenderDtoReq.calenderEnroll request);
}
