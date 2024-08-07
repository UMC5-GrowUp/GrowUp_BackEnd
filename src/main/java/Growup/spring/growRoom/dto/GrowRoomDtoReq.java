package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.GrowRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class GrowRoomDtoReq {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AddGrowRoomDtoReq {

        // growroom
        private Long recruitmentId; //모집 구분
        private Long numberId;  //모집 인원
        private Long periodId;  //진행기간
        private LocalDate startDate;
        private LocalDate endDate;

        // category
        private List<Long> categoryDetailIds;

        // post
        private String title;
        private String content;

        public GrowRoom toEntity(){

            return GrowRoom.builder()
                    .view(0)
                    .status("모집중")
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateGrowRoomDtoReq {

        // growroom
        private Long recruitmentId; //모집 구분
        private Long numberId;  //모집 인원
        private Long periodId;  //진행기간
        private LocalDate startDate;
        private LocalDate endDate;

        // category
        private List<Long> categoryDetailIds;

        // post
        private String title;
        private String content;
    }
}
