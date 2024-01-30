package Growup.spring.liked.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LikedDtoRes {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedRes{
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class unLikedRes{
        private Long growRoomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LiveRoomlikeRes{
        private LocalDateTime createdAt;
    }



}
