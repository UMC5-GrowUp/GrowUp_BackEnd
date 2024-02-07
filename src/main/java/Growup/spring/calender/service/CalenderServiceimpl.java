package Growup.spring.calender.service;

import Growup.spring.calender.converter.CalenderConverter;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.calender.dto.CalenderDtoRes;
import Growup.spring.calender.model.Calender;
import Growup.spring.calender.model.CalenderColor;
import Growup.spring.calender.model.Enum.CalenderColorStatus;
import Growup.spring.calender.model.Enum.CalenderStatus;
import Growup.spring.calender.repository.CalenderColorRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalenderServiceimpl implements CalenderService{
    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;
    private final CalenderColorRepository calenderColorRepository;

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

        CalenderColor calenderColor = calenderColorRepository.findByUserAndDay(user,day);


        if(calenderColor != null){
            CalenderColorStatus color = calenderColor.getColor();
            return CalenderConverter.calenderInquiryResultRes(userId,day,color,calenderInquiryResList);
        }else{
            CalenderColorStatus color = CalenderColorStatus.WHITE;
            return CalenderConverter.calenderInquiryResultRes(userId,day,color,calenderInquiryResList);
        }

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

        // 해당 유저와 날짜에 해당하는 캘린더 컬러 조회
        Map<LocalDate, CalenderColorStatus> calenderColors = calenderColorRepository.findByUserAndDayBetween(user, startOfMonth, endOfMonth)
                .stream()
                .collect(Collectors.toMap(CalenderColor::getDay, CalenderColor::getColor));

        List<CalenderDtoRes.calenderInquiryRes> calenderInquiryResList = calenderRepository.findByUser(user).stream()
                .filter(calenderList -> !calenderList.getDay().isBefore(startOfMonth) && !calenderList.getDay().isAfter(endOfMonth))
                .map(CalenderConverter::calenderInquiryRes)
                .collect(Collectors.toList());

        // 날짜별로 그룹화
        Map<LocalDate, List<CalenderDtoRes.calenderInquiryRes>> groupedByDay = calenderInquiryResList.stream()
                .collect(Collectors.groupingBy(CalenderDtoRes.calenderInquiryRes::getDay));

        List<CalenderDtoRes.calenderMonthInquiryRes> calenderMonthInquiryResList = parseDay.datesUntil(parseDay.plusMonths(1))
                .map(date -> {
                    List<CalenderDtoRes.calenderInquiryRes> inquiryResList = groupedByDay.getOrDefault(date, new ArrayList<>());
                    CalenderColorStatus color = calenderColors.getOrDefault(date, CalenderColorStatus.WHITE);
                    return CalenderConverter.calenderMonthInquiryRes(date,color,inquiryResList);
                })
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

    @Override
    //캘린더 색상 변경
    public void calenderColorModify(Long userId,CalenderDtoReq.calenderColorModify request){
        User user = userRepository.findById(userId).orElseThrow(()->new UserHandler(ErrorStatus.USER_NOT_FOUND));

        CalenderColor calenderColor = calenderColorRepository.findByUserAndDay(user,request.getDay());


        if(request.getColor()== CalenderColorStatus.WHITE) {
            if (calenderColor != null) {
                calenderColorRepository.delete(calenderColor);
            }
        }
        else{
            if (calenderColor != null) {
                calenderColor.setColor(request.getColor());
                calenderColorRepository.save(calenderColor);
            }
            else{
                calenderColorRepository.save(CalenderConverter.toCalenderColor(user,request));
            }
        }
    }
}
