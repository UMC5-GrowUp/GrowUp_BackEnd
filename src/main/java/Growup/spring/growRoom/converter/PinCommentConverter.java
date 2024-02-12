package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.repository.PinCommentRepository;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
}