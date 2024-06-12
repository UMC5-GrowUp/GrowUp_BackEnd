package Growup.spring.growRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class PinDtoReq {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AddPinDtoReq {

        private String comment;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdatePinDtoReq {

        private String comment;
    }
}
