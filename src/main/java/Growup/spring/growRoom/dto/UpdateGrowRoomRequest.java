package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.domain.component.GrowRoomCategory;
import Growup.spring.growRoom.domain.component.Number;
import Growup.spring.growRoom.domain.component.Period;
import Growup.spring.growRoom.domain.component.Recruitment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateGrowRoomRequest {

    //growroom
    private Recruitment recruitment; //모집 구분
    private Number number;  //모집 인원
    //모집 기간 X
    private Period period;  //진행기간

    // category
    private List<GrowRoomCategory> categoryList;    //카테고리 리스트(여러개)

    // post
    private String title;
    private String content;
}