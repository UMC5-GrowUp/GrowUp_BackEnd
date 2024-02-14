package Growup.spring.growRoom.service;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Pin;
import Growup.spring.growRoom.model.PinComment;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.growRoom.repository.PinCommentRepository;
import Growup.spring.growRoom.repository.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CleanupTask {

    private final GrowRoomRepository growRoomRepository;
    private final PinRepository pinRepository;
    private final PinCommentRepository pinCommentRepository;

    // 삭제상태로 하루가 지난 GrowRoom, Pin, PinComment는 삭제
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpired(){
        List<GrowRoom> expiredGrowRooms = growRoomRepository.findAllByStatusAndUpdatedAtBefore("삭제", LocalDateTime.now().minusDays(1));
        growRoomRepository.deleteAll(expiredGrowRooms);

        List<Pin> expiredPins = pinRepository.findAllByStatusAndUpdatedAtBefore("1", LocalDateTime.now().minusDays(1));
        pinRepository.deleteAll(expiredPins);

        List<PinComment> expiredPinComments = pinCommentRepository.findAllByStatusAndUpdatedAtBefore("1", LocalDateTime.now().minusDays(1));
        pinCommentRepository.deleteAll(expiredPinComments);
    }
}
