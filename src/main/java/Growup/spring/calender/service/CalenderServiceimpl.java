package Growup.spring.calender.service;

import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.model.Calender;
import Growup.spring.calender.repository.CalenderRepository;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalenderServiceimpl implements CalenderService{
    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    public Calender calenderEnroll(Long userId, CalenderDtoReq.calenderEnroll request){
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return calenderRepository.save(CalenderConverter.toCcalender(user,request));
    }
}
