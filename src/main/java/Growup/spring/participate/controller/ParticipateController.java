package Growup.spring.participate.controller;


import Growup.spring.constant.ApiResponse;
import Growup.spring.participate.converter.ParticipateConverter;
import Growup.spring.participate.dto.ParticipateDtoRes;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.service.ParticipateServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/participate")
public class ParticipateController {
    private final ParticipateServiceImpl participateService;

    //순서별로 조회
    @GetMapping("/orderBy")
    //filter를 사용 하여 구분 지음 filter를 사용 하여 각각의 순서대로 조회 되게 설정 , 기본값(null)으로 날짜순
    public ApiResponse <ParticipateDtoRes.orderByAsc> orderBy(@RequestParam(name = "filter",defaultValue = "createdate") String filter ,
                                                              @RequestParam(name = "growRoomId" ) Long growRoomId ,
                                                              @RequestParam(name = "page") Integer page) {

        Page<Participate> participateList = participateService.LiveupParticipateList(filter, growRoomId, page);

        return ApiResponse.onSuccess(ParticipateConverter.orderByAscDto(participateList.getContent())); //getcontent 를 통해 리스트로 변환


    }

}
