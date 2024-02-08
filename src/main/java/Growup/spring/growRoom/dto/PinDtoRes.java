package Growup.spring.growRoom.dto;


import Growup.spring.growRoom.model.Pin;
import lombok.Getter;

import java.time.LocalDateTime;

public class PinDtoRes {

    @Getter
    public static class PinViewDtoRes {

        private final String profilePic;
        private final String nickName;
        private final LocalDateTime createdAt;
        private final String comment;

        public PinViewDtoRes(Pin pin){
            this.profilePic = pin.getUser().getPhotoUrl();
            this.nickName = pin.getUser().getNickName();
            this.createdAt = pin.getCreatedAt();
            this.comment = pin.getComment();
        }
    }
}
