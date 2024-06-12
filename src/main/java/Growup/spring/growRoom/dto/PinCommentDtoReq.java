package Growup.spring.growRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PinCommentDtoReq {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AddPinCommentDtoReq {

        private String comment;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdatePinCommentDtoReq {

        private String comment;
    }
}
