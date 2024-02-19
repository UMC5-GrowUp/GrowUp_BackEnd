package Growup.spring.participate.dto;




import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.List;

public class ParticipateDtoRes {
    //참여자 그로우룸 입장- 응답
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class participateEnterRes{
        private Long growRoomId;
        private Long participateId;
        private LocalDateTime createdAd;
    }

    // 라이브룸 참여자 조회
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class participateInquiry{
        private Long userId;
        private String photoUrl;
        private String nickName;
        private Integer liked;
        private String totalTime;

    }
    // 라이브룸 참여자 조회 리스트
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class participateInquiryList{
        List<participateInquiry> participateInquiryList;
    }

    // 라이브룸 총 누적 시간 조회(참여자)-어제
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class liveRoomYesterdayTime{
        private Long growRoomId;
        private String totalTime;
    }

    // 라이브룸 랭킹 - 총 누적시간
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class liveRoomDateTimeRes{
        private Long growRoomId;
        private String photoUrl;
        private String nickName;
        private String totalTime;
    }


    // 라이브룸 랭킹 - 총 리스트
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class liveRoomDateTimeList{
        List<liveRoomDateTimeRes> liveRoomDateTimeResList;
    }

    //개인 누적시간 - 월간
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myTotalTime{
        private Long userId;
        private String TotalTime;
    }


    // 그로우룸의 주인 Id
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ownerRes {
        private Long userId;
    }
}
