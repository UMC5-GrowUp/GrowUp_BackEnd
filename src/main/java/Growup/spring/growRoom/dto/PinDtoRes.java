package Growup.spring.growRoom.dto;


import Growup.spring.growRoom.model.Pin;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PinDtoRes {

    @Getter
    public static class PinViewDtoRes {

        private final Long pinId;
        private final String profilePic;
        private final String nickName;
        private final LocalDateTime createdAt;
        private final String comment;
        private final List<PinCommentDtoRes.PinCommentViewDtoRes> pinComments;

        public PinViewDtoRes(Pin pin, List<PinCommentDtoRes.PinCommentViewDtoRes> pinComments){
            this.pinId = pin.getId();
            this.profilePic = pin.getUser().getPhotoUrl();
            this.nickName = pin.getUser().getNickName();
            this.createdAt = pin.getCreatedAt();
            this.comment = pin.getComment();

            this.pinComments = pinComments;
        }
    }
}
