package Growup.spring.participate.service;


import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.participate.converter.ParticipateConverter;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import Growup.spring.participate.repository.ParticipateRepository;
import Growup.spring.participate.repository.ParticipateTimeRepository;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService{

    private final ParticipateRepository participateRepository;
    private final GrowRoomRepository growRoomRepository;
    private final UserRepository userRepository;
    private final ParticipateTimeRepository participateTimeRepository;

    //참여자 입장
    @Override
    public ParticipateDtoRes.participateEnterRes participateEnter(Long userId, Long growRoomId) {
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //해당 그로우룸 존재 확인
        GrowRoom growRoom = growRoomRepository.findById(growRoomId).orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        // 참여자가 기준에 다다르거나 더 많으면 모집마감으로
        if (growRoom.getNumber().getNumber() >= growRoom.getParticipateList().size()) {
            growRoom.setStatus("모집마감");
            throw new GrowRoomHandler(ErrorStatus.PARTICIPATE_IS_FULL);
        }

        Participate participate = participateRepository.findByUserIdAndGrowRoomId(userId, growRoomId);

        //참여자 테이블에 참여자 생성(처음 입장시)
        if (participate == null) {
            participate = ParticipateConverter.toParticipate(user, growRoom);
            participateRepository.save(participate);
        }

        //입장시 입장시간 등록
        ParticipateTime participateTime = ParticipateConverter.toParticipateTime(participate);

        participateTimeRepository.save(participateTime);

        return ParticipateConverter.participateEnterRes(growRoomId, participate.getId());
    }

    //참여자 퇴장
    @Override
    public void participateOut(Long userId, Long growRoomId) {
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //해당 그로우룸 존재 확인
        //GrowRoom growRoom = growRoomRepository.findById(growRoomId).orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        Participate participate = participateRepository.findByUserIdAndGrowRoomId(userId, growRoomId);

        ParticipateTime participateTime = participateTimeRepository.findFirstByParticipateOrderByCreatedAtDesc(participate);

        LocalDateTime endTime = LocalDateTime.now().withNano(0);
        participateTime.setEndTime(endTime);

        participateTimeRepository.save(participateTime);

    }


    //라이브룸 참여자 조회
    @Override
    public ParticipateDtoRes.participateInquiryList participateInquiry(Long userId, Long growRoomId, String filter) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 필터
        List<Participate> participateList = null;
        if (filter.equals("랭킹순"))
            participateList = participateRepository.findByGrowRoomId(growRoomId);
        else if (filter.equals("이름순"))
            participateList = participateRepository.findByGrowRoomIdOrderByUser_NameAsc(growRoomId);
        else if (filter.equals("관심등록순"))
            participateList = participateRepository.findByGrowRoomIdAndLiked(growRoomId, 1); // like가 1인 것만 가져옴
        else {//전체순
            participateList = participateRepository.findByGrowRoomId(growRoomId);
        }
        // 오늘 날짜
        LocalDate today = LocalDate.now();

        if (participateList != null) { // null 체크
            // 각 참가자별로 총 참여 시간을 저장할 Map 생성
            Map<Long, Duration> totalTimeMap = new HashMap<>();

            // 각 참가자별로 총 참여 시간 계산하여 Map에 저장
            for (Participate participate : participateList) {
                Duration totalDuration = Duration.ZERO;
                for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                    // 퇴실 시간이 null이 아니라면 참여 시간 계산
                    if (participateTime.getEndTime() != null && participateTime.getStartTime().toLocalDate().isEqual(today)) {
                        totalDuration = totalDuration.plus(Duration.between(participateTime.getStartTime(), participateTime.getEndTime()));
                    }
                }
                totalTimeMap.put(participate.getId(), totalDuration);
            }

            if (filter.equals("랭킹순")) {
                // 참여 시간을 기준으로 참여자 리스트 정렬 (총 시간이 큰 순서대로)
                participateList.sort((p1, p2) -> totalTimeMap.get(p2.getId()).compareTo(totalTimeMap.get(p1.getId())));
            }
            // 각 참가자의 총 참여 시간을 응답에 추가
            List<ParticipateDtoRes.participateInquiry> participateInquiryList = participateList.stream()
                    .map(participate -> {
                        Duration totalDuration = totalTimeMap.getOrDefault(participate.getId(), Duration.ZERO);
                        String formattedDuration = formatDuration(totalDuration); // 시간을 시:분:초 형식으로 변환
                        return ParticipateConverter.participateInquiry(participate, formattedDuration);
                    })
                    .collect(Collectors.toList());

            return ParticipateConverter.participateInquiryList(participateInquiryList);
        } else
            // 참가자가 없는 경우 처리
            return ParticipateConverter.participateInquiryList(Collections.emptyList());
    }

    // 시간을 시:분:초 형식으로 변환하는 메서드
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    //해당 방의 전 날 누적 시간 - 모든 참여자
    public ParticipateDtoRes.liveRoomYesterdayTime liveRoomYesterdayTimeInquiry(Long growRoomId) {

        LocalDate yesterday = LocalDate.now().minusDays(1); // 어제 날짜

        List<Participate> participateList = participateRepository.findByGrowRoomId(growRoomId);

        Duration totalDuration = Duration.ZERO;

        if (!(participateList == null)) {
            for (Participate participate : participateList) {
                List<ParticipateTime> participateTimeList = participate.getParticipateTimeList().stream()
                        .filter(participateTime -> participateTime.getEndTime().toLocalDate().isEqual(yesterday))
                        .collect(Collectors.toList());
                // 각 참가자의 어제의 누적 시간 계산
                for (ParticipateTime participateTime : participateTimeList) {
                    totalDuration = totalDuration.plus(Duration.between(participateTime.getStartTime(), participateTime.getEndTime()));
                }
            }
            return ParticipateConverter.liveRoomYesterdayTime(growRoomId, formatDuration(totalDuration));
        }

        return ParticipateConverter.liveRoomYesterdayTime(growRoomId, "00:00:00");
    }

    //월/주/일간 랭킹 참여자 누적시간
    @Override
    public List<ParticipateDtoRes.liveRoomDateTimeRes> liveRoomDateTimeRank(String filter) {

        LocalDate today = LocalDate.now(); // 오늘 날짜
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1); // 이전 달의 첫째 날
        LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth()); // 이전 달의 마지막 날

        List<Participate> participateList = participateRepository.findAll();

        List<ParticipateDtoRes.liveRoomDateTimeRes> top10List = null;

        if (filter.equals("일간")) {
            // 일간 필터를 적용하는 경우
            top10List = liveRoomDailyRank(today, participateList);
        } else if (filter.equals("월간")) {
            // 월간 필터를 적용하는 경우
            top10List = liveRoomMonthlyRank(today, lastDayOfLastMonth, participateList);
        } else if (filter.equals("주간")) {
            top10List = liveRoomWeeklyRank(today, participateList);
        }

        return top10List;

    }

    // 일간 랭킹을 계산하는 메서드
    private List<ParticipateDtoRes.liveRoomDateTimeRes> liveRoomDailyRank(LocalDate today, List<Participate> participateList) {
        LocalDate yesterday = today.minusDays(1);

        // 각 참가자의 어제의 누적 시간을 저장할 Map 생성
        Map<GrowRoom, Duration> totalTimeMap = new HashMap<>();

        for (Participate participate : participateList) {
            Duration totalDuration = totalTimeMap.getOrDefault(participate.getGrowRoom(), Duration.ZERO);

            for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                LocalDateTime endTime = participateTime.getEndTime();

                // 어제의 참여 시간만 고려
                if (endTime != null && endTime.toLocalDate().isEqual(yesterday)) {
                    LocalDateTime startTime = participateTime.getStartTime();
                    totalDuration = totalDuration.plus(Duration.between(startTime, endTime));
                }
            }

            totalTimeMap.put(participate.getGrowRoom(), totalDuration);
        }

        // 누적 시간을 기준으로 내림차순으로 정렬
        List<Map.Entry<GrowRoom, Duration>> sortedList = new ArrayList<>(totalTimeMap.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // 상위 10개만 추출
        return ParticipateConverter.liveRoomDateTimeList(sortedList);
    }

    // 주간 랭킹을 계산하는 메서드
    private List<ParticipateDtoRes.liveRoomDateTimeRes> liveRoomWeeklyRank(LocalDate today, List<Participate> participateList) {
        // 주간 누적 시간을 저장할 Map 생성
        Map<GrowRoom, Duration> totalTimeMap = new HashMap<>();

        LocalDate startOfWeek;
        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            // 현재 날짜가 월요일인 경우, 이전 주의 월요일부터 일요일까지의 누적 시간을 구함
            startOfWeek = today.minusWeeks(1).with(DayOfWeek.MONDAY); // 이전 주의 월요일로 설정
        } else {
            // 현재 날짜가 월요일이 아닌 경우, 현재 주의 월요일부터 어제까지의 누적 시간을 구함
            startOfWeek = today.with(DayOfWeek.MONDAY); // 현재 주의 월요일로 설정
        }

        LocalDate endOfWeek = today.minusDays(1); // 어제까지의 날짜를 기준으로 함

        for (Participate participate : participateList) {
            Duration totalDuration = totalTimeMap.getOrDefault(participate.getGrowRoom(), Duration.ZERO);

            for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                LocalDateTime startTime = participateTime.getStartTime();
                LocalDateTime endTime = participateTime.getEndTime();

                if (startTime != null && endTime != null &&
                        (startTime.toLocalDate().isEqual(startOfWeek) || startTime.toLocalDate().isAfter(startOfWeek))
                        && (startTime.toLocalDate().isBefore(endOfWeek) || startTime.toLocalDate().isEqual(endOfWeek))) {
                    // 해당 주의 누적 시간을 계산
                    totalDuration = totalDuration.plus(Duration.between(startTime, endTime));
                }
            }
            totalTimeMap.put(participate.getGrowRoom(), totalDuration);
        }

        // 누적 시간을 기준으로 내림차순으로 정렬
        List<Map.Entry<GrowRoom, Duration>> sortedList = new ArrayList<>(totalTimeMap.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // 상위 10개만 추출
        List<Map.Entry<GrowRoom, Duration>> top10List = sortedList.subList(0, Math.min(sortedList.size(), 10));

        // 상위 10개의 결과를 변환하여 반환
        return ParticipateConverter.liveRoomDateTimeList(top10List);
    }



    // 월간 랭킹을 계산하는 메서드
    private List<ParticipateDtoRes.liveRoomDateTimeRes> liveRoomMonthlyRank(LocalDate today, LocalDate lastDayOfLastMonth, List<Participate> participateList) {

        LocalDate firstDayOfMonth = today.withDayOfMonth(1); // 해당 달의 첫째 날
        // 각 그로우 룸당 이전 달의 누적 시간을 저장할 Map 생성
        Map<GrowRoom, Duration> totalTimeMap = new HashMap<>();

        if (today.getDayOfMonth() == 1) {
            // 매달 1일인 경우에는 저번 달의 누적 시간을 계산
            LocalDate firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);

            for (Participate participate : participateList) {
                Duration totalDuration = totalTimeMap.getOrDefault(participate.getGrowRoom(), Duration.ZERO);

                for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                    LocalDateTime startTime = participateTime.getStartTime();
                    LocalDateTime endTime = participateTime.getEndTime();
                    if (startTime != null && endTime != null && startTime.toLocalDate().isAfter(firstDayOfLastMonth.minusDays(1)) &&
                            startTime.toLocalDate().isBefore(lastDayOfLastMonth.plusDays(1))) {
                        totalDuration = totalDuration.plus(Duration.between(startTime, endTime));
                    }
                }
                totalTimeMap.put(participate.getGrowRoom(), totalDuration);
            }
        }else {
            // 현재 달의 누적 시간을 계산
            for (Participate participate : participateList) {
                Duration totalDuration = totalTimeMap.getOrDefault(participate.getGrowRoom(), Duration.ZERO);

                for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                    LocalDateTime endTime = participateTime.getEndTime();

                    // 해당 달의 참여 시간만 고려
                    if (endTime != null && endTime.toLocalDate().isAfter(firstDayOfMonth.minusDays(1)) && endTime.toLocalDate().isBefore(today)) {
                        LocalDateTime startTime = participateTime.getStartTime();
                        totalDuration = totalDuration.plus(Duration.between(startTime, endTime));
                    }
                }

                totalTimeMap.put(participate.getGrowRoom(), totalDuration);
            }
        }

        // 누적 시간을 기준으로 내림차순으로 정렬
        List<Map.Entry<GrowRoom, Duration>> sortedList = new ArrayList<>(totalTimeMap.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // 상위 10개만 추출
        List<Map.Entry<GrowRoom, Duration>> top10List = sortedList.subList(0, Math.min(sortedList.size(), 10));

        // 상위 10개의 결과를 변환하여 반환
        return ParticipateConverter.liveRoomDateTimeList(top10List);
    }


    //개인 총 누적시간 계산
    public Duration liveRoomMyTotalTime(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 현재 날짜
        LocalDate today = LocalDate.now();
        // 이번 달의 첫째 날
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        Duration totalDuration = Duration.ZERO;

        // 오늘이 1일인 경우, 지난 달의 첫째 날부터 마지막 날까지를 대상으로 함
        if (today.getDayOfMonth() == 1) {
            // 지난 달의 첫째 날
            LocalDate firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);
            // 지난 달의 마지막 날
            LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());
            List<Participate> participateList = participateRepository.findAllByUserId(user.getId());

            for (Participate participate : participateList) {
                for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                    LocalDateTime startTime = participateTime.getStartTime();
                    LocalDateTime endTime = participateTime.getEndTime();

                    // 해당 기간에 속하는 참여 시간만 고려
                    if (startTime != null && endTime != null
                            && startTime.toLocalDate().isAfter(firstDayOfLastMonth.minusDays(1))
                            && startTime.toLocalDate().isBefore(lastDayOfLastMonth.plusDays(1))) {
                        // 누적 시간 계산
                        totalDuration = totalDuration.plus(Duration.between(startTime, endTime));
                    }
                }

            }

            // 결과 출력 또는 처리
        } else {

            List<Participate> participateList = participateRepository.findAllByUserId(user.getId());

            for (Participate participate : participateList) {
                for (ParticipateTime participateTime : participate.getParticipateTimeList()) {
                    LocalDateTime startTime = participateTime.getStartTime();
                    LocalDateTime endTime = participateTime.getEndTime();

                    // 해당 기간에 속하는 참여 시간만 고려
                    if (endTime != null && endTime.toLocalDate().isAfter(firstDayOfMonth.minusDays(1)) && endTime.toLocalDate().isBefore(today)) {
                        // 누적 시간 계산
                        totalDuration = totalDuration.plus(Duration.between(startTime, endTime));
                    }
                }
            }
        }
        return totalDuration;

    }






    //자정이 되면 모든 그로우룸 참여자 퇴실처리및 자동 입장처리
    @Scheduled(cron = "0 0 0 * * *") // 매일 새벽 2시 20분에 실행
    public void participateModifyToday(){
        LocalDate today = LocalDate.now();

        List<GrowRoom> growRoomList = growRoomRepository.findAllByStatus("모집중");

        for(GrowRoom growRoom : growRoomList) {
            List<Participate> participateList = growRoom.getParticipateList();

            for (Participate participate : participateList) {
                // 퇴실하지 않은 참여자의 participateTime을 처리
                List<ParticipateTime> participateTimes = participate.getParticipateTimeList().stream()
                        .filter(participateTime -> participateTime.getEndTime() == null ||!participateTime.getStartTime().toLocalDate().isEqual(today)) // endtime이 null인 것만 필터링
                        .collect(Collectors.toList());
                for (ParticipateTime participateTime : participateTimes) {
                    // endtime을 24:00:00으로 설정
                    participateTime.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));

                    participateTimeRepository.save(ParticipateConverter.toParticipateTime(participate));

                }

            }
        }
    }


    @Override
    public Long findOwner(Long growRoomId) {
        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
        return growRoom.getUser().getId();
    }


}
