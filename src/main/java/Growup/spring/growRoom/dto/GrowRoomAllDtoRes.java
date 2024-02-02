package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.GrowRoom;
import lombok.Getter;

@Getter
public class GrowRoomAllDtoRes {

//    private final String 인기글
    private final String recruitment_field;    // 모집 구분
    private final String status;    // 상태 (모집중, 모집완료)
//    private final String 마감일
    private final String title; // title or content
    private final Integer view;
//    private final String liked;   // 좋아요 여부

    public GrowRoomAllDtoRes(GrowRoom growRoom){
        // 인기글
        this.recruitment_field = growRoom.getRecruitment().getField();
        this.status = growRoom.getStatus();
        // 마감일
        this.title = growRoom.getPost().getTitle();
        this.view = growRoom.getView();
        // 좋아요 여부
    }
}
