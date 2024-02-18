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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class orderByAsc {
        List<participateRes> participateResList; //participateRes를 가져옴

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class participateRes {

        private String nickName;
        private Integer liked;
        private String photoUrl;

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
