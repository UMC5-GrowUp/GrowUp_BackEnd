package Growup.spring.calender.service;

import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.model.Calender;
import Growup.spring.calender.model.Enum.CalenderStatus;
import Growup.spring.calender.repository.CalenderRepository;
import Growup.spring.constant.handler.CalenderHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalenderServiceimpl implements CalenderService{
    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    @Override
    //캘린더 등록
    public Calender calenderEnroll(Long userId, CalenderDtoReq.calenderEnroll request){
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return calenderRepository.save(CalenderConverter.toCcalender(user,request));
    }

    @Override
    //캘린더 특정 날짜 목록 조회
    public CalenderDtoRes.calenderInquiryResultRes calenderInquiry(LocalDate day, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        List<CalenderDtoRes.calenderInquiryRes> calenderInquiryResList = calenderRepository.findByUser(user).stream()
                .filter(calenderList -> calenderList.getDay().isEqual(day))
                .map(CalenderConverter::calenderInquiryRes)
                .collect(Collectors.toList());

        return CalenderConverter.calenderInquiryResultRes(userId,day,calenderInquiryResList);

    }

    @Override
    //캘린더 해당 달 전체 조회
    public CalenderDtoRes.calenderMonthInquiryResultRes calenderMonthInquiry(Long userId, YearMonth day){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));


        // 쿼리스트링에서 전달받은 날짜를 LocalDate로 파싱
        LocalDate parseDay = LocalDate.parse(day + "-01");
        // 해당 월의 시작일과 종료일 계산
        LocalDate startOfMonth = parseDay.withDayOfMonth(1);
        LocalDate endOfMonth = YearMonth.from(parseDay).atEndOfMonth();

        List<CalenderDtoRes.calenderInquiryRes> calenderInquiryResList = calenderRepository.findByUser(user).stream()
                .filter(calenderList -> !calenderList.getDay().isBefore(startOfMonth) && !calenderList.getDay().isAfter(endOfMonth))
                .map(CalenderConverter::calenderInquiryRes)
                .collect(Collectors.toList());

        // 날짜별로 그룹화
        Map<LocalDate, List<CalenderDtoRes.calenderInquiryRes>> groupedByDay = calenderInquiryResList.stream()
                .collect(Collectors.groupingBy(CalenderDtoRes.calenderInquiryRes::getDay));


        List<CalenderDtoRes.calenderMonthInquiryRes> calenderMonthInquiryResList =parseDay.datesUntil(parseDay.plusMonths(1))
                .map(date ->CalenderConverter.calenderMonthInquiryRes(date,groupedByDay))
                .collect(Collectors.toList());

        // 결과 생성
        return CalenderConverter.calenderMonthInquiryResultRes(userId,calenderMonthInquiryResList);

    }


    @Override
    //글(목록) 수정
    public void calenderCommentModify(Long userId, CalenderDtoReq.calenderCommentModify request){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Calender calender = calenderRepository.findById(request.getCalenderId()).orElseThrow(()->new CalenderHandler(ErrorStatus.CALENDER_NOT_FOUND));

        calender.setComment(request.getComment());

        calenderRepository.save(calender);

    }
    @Override
    //글(목록) 줄 긋기
    public void calenderStatusModify(Long calenderId){
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(()->new CalenderHandler(ErrorStatus.CALENDER_NOT_FOUND));

        if (calender.getStatus()== CalenderStatus.ACTIVE ){
            calender.setStatus(CalenderStatus.NONACTIVE);
        }
        else{
            calender.setStatus(CalenderStatus.ACTIVE);
        }

        calenderRepository.save(calender);
    }
    @Override
    //글(목록) 삭제
    public void calenderDelete(Long calenderId){
        calenderRepository.findById(calenderId).orElseThrow(()->new CalenderHandler(ErrorStatus.CALENDER_NOT_FOUND));

        calenderRepository.deleteById(calenderId);
    }

}
