package Growup.spring.calender.dto;

import Growup.spring.calender.model.Enum.CalenderColorStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CalenderDtoReq {

    @Getter
    public static class calenderEnroll{
        @NotBlank
        private String comment;
        @NotNull
        private LocalDate day;
    }

    @Getter
    public static class calenderCommentModify{
        @NotNull
        private Long calenderId;
        @NotBlank
        private String comment;
    }

    @Getter
    public static class calenderColorModify{
        @NotNull
        private LocalDate day;
        @NotNull
        private CalenderColorStatus color;
    }




}
