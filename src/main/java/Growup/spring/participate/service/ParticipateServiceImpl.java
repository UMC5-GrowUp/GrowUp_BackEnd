package Growup.spring.participate.service;



import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.repository.ParticipateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService{

    private final ParticipateRepository participateRepository;
    private final GrowRoomRepository growRoomRepository;

    @Override
    public List<Participate> LiveupParticipateList (String filter , Long growRoomId){

        List<Participate> list = participateRepository.findAllByGrowRoomId(growRoomId);

        if(!list.isEmpty() ) {
            if (filter.equals("Ranking")) {
                 list = participateRepository.findAllByGrowRoomIdOrderByParticipateTimeAsc(growRoomId); //많은 시간을 참여한 순서대로 출력
            } else if (filter.equals("Name")) {
                 list = participateRepository.findAllByGrowRoomIdOrderByUser_nickNameAsc(growRoomId); // 이름 순으로 가져옴
            } else if (filter.equals("Liked")) {
                 list =participateRepository.findAllByGrowRoomIdOrderByLikedAsc(growRoomId); // 관심등록이 된것들만 출력
            } else {
                 list= participateRepository.findAllByGrowRoomIdOrderByCreatedAtDesc(growRoomId); //방에 들어온 순서대로 출력
            }
        }
        else {
            throw new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND);
        }
        return list;
    }

}
