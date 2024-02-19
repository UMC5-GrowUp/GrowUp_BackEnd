package Growup.spring.participate.service;

import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import org.springframework.data.domain.Page;

import java.time.Duration;
import java.util.List;

public interface ParticipateService {
    ParticipateDtoRes.participateEnterRes participateEnter(Long userId, Long growRoomId);

    void participateOut(Long userId, Long growRoomId);

    ParticipateDtoRes.participateInquiryList participateInquiry(Long userId, Long growRoomId,String filter);

    ParticipateDtoRes.liveRoomYesterdayTime liveRoomYesterdayTimeInquiry(Long growRoomId);

    List<ParticipateDtoRes.liveRoomDateTimeRes> liveRoomDateTimeRank(String filter);

    Duration liveRoomMyTotalTime(Long userId);

    Long findOwner(Long growRoomId);

}
