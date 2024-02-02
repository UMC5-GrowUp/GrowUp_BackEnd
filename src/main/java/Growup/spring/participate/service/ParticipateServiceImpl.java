package Growup.spring.participate.service;


import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.repository.ParticipateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService{

    private final ParticipateRepository participateRepository;
    private final GrowRoomRepository growRoomRepository;

    @Override
    public Page<Participate> LiveupParticipateList (String filter , Long growRoomId , Integer page ){
        boolean existgrowroom = growRoomRepository.existsById(growRoomId);

        Page<Participate> list;

        if(existgrowroom) {
            if (filter.equals("Ranking")) {
                list = participateRepository.findAllByGrowRoomIdOrderByParticipateTimeAsc(growRoomId , PageRequest.of(page,20));//많은 시간을 참여한 순서대로 출력
            } else if (filter.equals("Name")) {
                list = participateRepository.findAllByGrowRoomIdOrderByUser_nickNameAsc(growRoomId , PageRequest.of(page,20)); // 이름 순으로 가져옴
            } else if (filter.equals("Liked")) {
                list =participateRepository.findAllByGrowRoomIdOrderByLikedAsc(growRoomId , PageRequest.of(page,20)); // 관심등록이 된것들만 출력
            } else {
                list = participateRepository.findAllByGrowRoomIdOrderByCreatedAtDesc(growRoomId , PageRequest.of(page,20)); //방에 들어온 순서대로 출력
            }
        }
        else {
            throw new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND);
        }
        return list;
    }

}
