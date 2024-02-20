package Growup.spring.participate.converter;


import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipateConverter {

    //참여자 생성
    public static Participate toParticipate(User user, GrowRoom growroom) {
        return Participate.builder()
                .user(user)
                .growRoom(growroom)
                .build();
    }

    //참여자 생성시 참여시간 생성
    public static ParticipateTime toParticipateTime(Participate participate){
        LocalDateTime time = LocalDateTime.now().withNano(0);
        return ParticipateTime.builder()
                .startTime(time)
                .participate(participate)
                .build();
    }
    // 자정시 참여시간 재생성
    public static ParticipateTime toReParticipateTime(Participate participate){
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        return ParticipateTime.builder()
                .startTime(midnight)
                .participate(participate)
                .build();
    }

    //참여자 입장시 응답
    public static ParticipateDtoRes.participateEnterRes participateEnterRes(Long growRoomId){
        return ParticipateDtoRes.participateEnterRes.builder()
                .growRoomId(growRoomId)
                .createdAd(LocalDateTime.now())
                .build();
    }

    //참여자 조회
    public static ParticipateDtoRes.participateInquiry participateInquiry(Participate participate,String formattedDuration){
        return ParticipateDtoRes.participateInquiry.builder()
                .participateId(participate.getId())
                .nickName(participate.getUser().getNickName())
                .photoUrl(participate.getUser().getPhotoUrl())
                .totalTime(formattedDuration)
                .liked(participate.getLiked())
                .build();
    }

    //참여자 조회 리스트
    public static ParticipateDtoRes.participateInquiryList participateInquiryList(List<ParticipateDtoRes.participateInquiry> list){
        return ParticipateDtoRes.participateInquiryList.builder()
                .participateInquiryList(list)
                .build();
    }

    // 라이브룸 총 누적 시간(일간)-어제
    public static ParticipateDtoRes.liveRoomYesterdayTime liveRoomYesterdayTime(Long growroomId,String time){
        return ParticipateDtoRes.liveRoomYesterdayTime.builder()
                .growRoomId(growroomId)
                .totalTime(time)
                .build();
    }


    //일간 누적시간 상위 10개
    public static List<ParticipateDtoRes.liveRoomDateTimeRes> liveRoomDateTimeList(List<Map.Entry<GrowRoom, Duration>> sortedList){
        return sortedList.subList(0, Math.min(sortedList.size(), 10))
                .stream()
                .map(entry -> ParticipateDtoRes.liveRoomDateTimeRes.builder()
                        .growRoomId(entry.getKey().getId())
                        .photoUrl(entry.getKey().getUser().getPhotoUrl())
                        .nickName(entry.getKey().getUser().getNickName())
                        .totalTime(formatDuration(entry.getValue()))
                        .build())
                .collect(Collectors.toList());
    }
    //일간 누적 시간 상위 10개 리스트
    public static ParticipateDtoRes.liveRoomDateTimeList liveRoomDateTimeResList(List<ParticipateDtoRes.liveRoomDateTimeRes> list){
        return ParticipateDtoRes.liveRoomDateTimeList.builder()
                .liveRoomDateTimeResList(list)
                .build();
    }

    //개인 누적시간
    public static ParticipateDtoRes.myTotalTime myTotalTime(Long userId,Duration duration){
        return ParticipateDtoRes.myTotalTime.builder()
                .userId(userId)
                .TotalTime(formatDuration(duration))
                .build();
    }


    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }



}
