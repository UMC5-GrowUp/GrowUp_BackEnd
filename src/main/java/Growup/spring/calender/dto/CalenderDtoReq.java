package Growup.spring.calender.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CalenderDtoReq {

    @Getter
    public static class calenderEnroll{
        @NotBlank
        private String comment;
        @NotBlank
        private LocalDate day;
    }

}
