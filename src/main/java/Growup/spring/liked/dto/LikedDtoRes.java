package Growup.spring.liked.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LikedDtoRes {

    @Getter
    public static class Liked{
        private final boolean liked;

        public Liked(boolean liked) {
            this.liked = liked;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LiveRoomlikeRes {
        private LocalDateTime createdAt;
    }
}