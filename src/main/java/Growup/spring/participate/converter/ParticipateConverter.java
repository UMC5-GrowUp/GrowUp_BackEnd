package Growup.spring.participate.converter;


import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipateConverter {

    public static ParticipateDtoRes.participateRes participateDto (Participate participate){
        return ParticipateDtoRes.participateRes.builder()
                .nickName(participate.getUser().getNickName()) // user를 가져와 다시 nickname을 가져옴
                .liked(participate.getLiked())
                .is50Up(participate.getIs_50up())
                .isBestUp(participate.getIs_bestup())
                .participateTime(participate.getParticipateTime())
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
