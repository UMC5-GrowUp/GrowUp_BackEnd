package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.dto.PinCommentDtoRes;
import Growup.spring.growRoom.dto.PinDtoRes;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.service.PinCommentService;
import Growup.spring.growRoom.service.PinService;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinConverter {

    public Pin convertToPin(GrowRoom growRoom, User user, String comment){
        return Pin.builder()
                .growRoom(growRoom)
                .user(user)
                .comment(comment)
                .status("0")
                .build();
    }
}
