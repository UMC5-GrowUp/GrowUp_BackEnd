package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.repository.PinRepository;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PinConverter {
    private final PinRepository pinRepository;

    public Pin convertToPin(GrowRoom growRoom, User user, String comment){
        return Pin.builder()
                .growRoom(growRoom)
                .user(user)
                .comment(comment)
                .status("0")
                .build();
    }
}
