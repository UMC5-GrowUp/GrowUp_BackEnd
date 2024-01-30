package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.component.GrowRoomCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateGrowRoomDtoReq {

    //growroom
    private Long recruitmentId; //모집 구분
    private Long numberId;  //모집 인원
    //모집 기간 X
    private Long periodId;  //진행기간

    // category
    private List<GrowRoomCategory> categoryList;    //카테고리 리스트(여러개)

    // post
    private String title;
    private String content;
}