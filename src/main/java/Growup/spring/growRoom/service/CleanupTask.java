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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CleanupTask {

    private final GrowRoomRepository growRoomRepository;
    private final PinRepository pinRepository;
    private final PinCommentRepository pinCommentRepository;

    // 삭제상태로 하루가 지난 GrowRoom, Pin, PinComment는 삭제
    @Scheduled(cron = "0 0 0 * * ?")    // 매일 00시 00분 마다 실행
    public void deleteExpired(){
        List<GrowRoom> expiredGrowRooms = growRoomRepository.findAllByStatusAndUpdatedAtBefore("삭제", LocalDateTime.now().minusDays(1));
        growRoomRepository.deleteAll(expiredGrowRooms);

        List<Pin> expiredPins = pinRepository.findAllByStatusAndUpdatedAtBefore("1", LocalDateTime.now().minusDays(1));
        pinRepository.deleteAll(expiredPins);

        List<PinComment> expiredPinComments = pinCommentRepository.findAllByStatusAndUpdatedAtBefore("1", LocalDateTime.now().minusDays(1));
        pinCommentRepository.deleteAll(expiredPinComments);
    }


//    @Scheduled(fixedRate = 60000)    // 10초마다 갱신
//    public void endRecruitment(){
//        List<GrowRoom> growRooms = growRoomRepository.findAll();
//        for (GrowRoom growRoom : growRooms) {
            // 나가는 것 까지 고려한 로직
//            if (growRoom.getParticipateList().size() >= growRoom.getNumber().getNumber()) {
//                growRoom.setStatus("모집마감");
//            }
//        }
//    }

    @Scheduled(cron = "0 0 0 * *")  // 매일 00시 00분마다 실행
    public void endRecruitmentDate() {
        List<GrowRoom> growRooms = growRoomRepository.findAll();
        // 기간이 지나면 모집마감
        for (GrowRoom growRoom : growRooms) {
            if (growRoom.getRecruitmentPeriod().getEndDate() == LocalDateTime.now().toLocalDate()) {// 임마는 하루마다 갱신 해야함
                growRoom.setStatus("모집마감");
            }
        }
    }
}
