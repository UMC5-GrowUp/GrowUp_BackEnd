package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.dto.PinCommentDtoRes;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.service.PinCommentService;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinCommentConverter {

    public PinComment convertToPinComment(Pin pin, User user, String comment){
        return PinComment.builder()
                .pin(pin)
                .user(user)
                .comment(comment)
                .status("0")
                .build();
    }

    public List<PinCommentDtoRes.PinCommentViewDtoRes> convertToPinCommentRes(List<PinComment> pinComments){
        List<PinCommentDtoRes.PinCommentViewDtoRes> res = new ArrayList<>();
        for (PinComment pinComment : pinComments) {
            res.add(new PinCommentDtoRes.PinCommentViewDtoRes(pinComment));
        }
        return res;
    }
}