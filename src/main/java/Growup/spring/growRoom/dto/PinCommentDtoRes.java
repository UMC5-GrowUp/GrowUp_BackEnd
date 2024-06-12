package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.PinComment;
import lombok.Getter;

import java.time.LocalDateTime;

public class PinCommentDtoRes {

    @Getter
    public static class PinCommentViewDtoRes {

        private final Long pinCommentId;
        private final String profilePic;
        private final String nickName;
        private final LocalDateTime createdAt;
        private final String comment;

        public PinCommentViewDtoRes(PinComment pinComment){
            this.pinCommentId = pinComment.getId();
            this.profilePic = pinComment.getUser().getPhotoUrl();
            this.nickName = pinComment.getUser().getNickName();
            this.createdAt = pinComment.getCreatedAt();
            this.comment = pinComment.getComment();
        }
    }
}
