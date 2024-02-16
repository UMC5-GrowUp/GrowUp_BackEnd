package Growup.spring.participate.converter;


import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.model.ParticipateTime;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipateConverter {

    //참여자 생성
    public static Participate toParticipate(User user, GrowRoom growroom) {
        return Participate.builder()
                .user(user)
                .growRoom(growroom)
                .build();
    }

    //참여자 생성시 참여시간 생성
    public static ParticipateTime toParticipateTime(Participate participate){
        return ParticipateTime.builder()
                .participate(participate)
                .build();
    }

    //참여자 입장시 응답
    public static ParticipateDtoRes.participateEnterRes participateEnterRes(Long growRoomId, Long participateId){
        return ParticipateDtoRes.participateEnterRes.builder()
                .growRoomId(growRoomId)
                .participateId(participateId)
                .createdAd(LocalDateTime.now())
                .build();
    }



    public static ParticipateDtoRes.participateRes participateDto (Participate participate){
        return ParticipateDtoRes.participateRes.builder()
                .nickName(participate.getUser().getNickName()) // user를 가져와 다시 nickname을 가져옴
                .liked(participate.getLiked())
                .photoUrl(participate.getUser().getPhotoUrl())
                .build();
    }

    public static ParticipateDtoRes.orderByAsc orderByAscDto (List<Participate> participateList){

        // 다른 converter를 가져오는 법
        List<ParticipateDtoRes.participateRes> participateResList = participateList.stream()
                .map(ParticipateConverter::participateDto)
                .collect(Collectors.toList());

        //다른 converter 참조해서 참여자 리스트를 가져옴
        return ParticipateDtoRes.orderByAsc.builder()
                .participateResList(participateResList)
                .build();
    }



}
