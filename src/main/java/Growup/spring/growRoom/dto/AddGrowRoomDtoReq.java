package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddGrowRoomDtoReq {

    // growroom
    private Long recruitmentId; //모집 구분
    private Long numberId;  //모집 인원
    private Long periodId;  //진행기간

    // category
    private List<Long> categoryDetailIds;

    // post
    private String title;
    private String content;

    public GrowRoom toEntity(){

        return GrowRoom.builder()
                .view(0)
                .status("모집중")
                .build();
    }
}
