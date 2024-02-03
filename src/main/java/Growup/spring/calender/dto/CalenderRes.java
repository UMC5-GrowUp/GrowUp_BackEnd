package Growup.spring.calender.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CalenderRes {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class calenderEnrollRes{
        private Long userId;
        private Long calenderId;
        private LocalDateTime createAt;
    }
}
