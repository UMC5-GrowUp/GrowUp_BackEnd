package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddGrowRoomDtoReq {

    // growroom
    private Long recruitmentId; //모집 구분
    private Long numberId;  //모집 인원
    private Long periodId;  //진행기간

    // category
    private Long categoryDetailId1;
    private Long categoryDetailId2;
    private Long categoryDetailId3;

    // post
    private String title;
    private String content;

    public GrowRoom toEntity(){
        GrowRoom growRoom = GrowRoom.builder()
                .view(0)
                .status("모집중")
                .build();

        return growRoom;
    }
}
