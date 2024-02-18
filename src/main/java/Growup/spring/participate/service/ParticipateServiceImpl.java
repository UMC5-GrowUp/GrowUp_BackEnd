package Growup.spring.participate.service;


import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.participate.converter.ParticipateConverter;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import Growup.spring.participate.repository.ParticipateRepository;
import Growup.spring.participate.repository.ParticipateTimeRepository;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService{

    private final ParticipateRepository participateRepository;
    private final GrowRoomRepository growRoomRepository;
    private final UserRepository userRepository;
    private final ParticipateTimeRepository participateTimeRepository;

    @Override
    public ParticipateDtoRes.participateEnterRes participateEnter(Long userId, Long growRoomId){
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //해당 그로우룸 존재 확인
        GrowRoom growRoom = growRoomRepository.findById(growRoomId).orElseThrow(()-> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        Participate participate = participateRepository.findByUserIdAndGrowRoomId(userId,growRoomId);

        //참여자 테이블에 참여자 생성(처음 입장시)
        if(participate == null) {
            participate = ParticipateConverter.toParticipate(user, growRoom);
            participateRepository.save(participate);
        }

        //입장시 입장시간 등록
        ParticipateTime participateTime = ParticipateConverter.toParticipateTime(participate);

        participateTimeRepository.save(participateTime);

        return ParticipateConverter.participateEnterRes(growRoomId, participate.getId());
    }

    @Override
    public void participateOut(Long userId, Long growRoomId){
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //해당 그로우룸 존재 확인
        GrowRoom growRoom = growRoomRepository.findById(growRoomId).orElseThrow(()-> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        Participate participate = participateRepository.findByUserIdAndGrowRoomId(userId,growRoomId);

        ParticipateTime participateTime = participateTimeRepository.findFirstByParticipateOrderByCreatedAtDesc(participate);

        participateTime.setEndTime(LocalDateTime.now());

    }

    @Override
    public Long findOwner(Long growRoomId) {
        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
        return growRoom.getUser().getId();
    }

    /*
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


     */
}
