package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import lombok.Getter;

import java.util.List;


@Getter
public class GrowRoomDtoRes {

    // user
    private final String nick_name;
    private final String photo_url;

    // growroom
    private final String recruitment_field;
    private final Integer number;
    private final String period;

    // category - 최대 3개
//    private final String categoryDetail_name;
    private final String categoryListDetail0;
    private final String categoryListDetail1;
    private final String categoryListDetail2;

    // post
    private final String title;
    private final String content;

    public GrowRoomDtoRes(GrowRoom growRoom) {
        this.nick_name = growRoom.getUser().getNickName();
        this.photo_url = growRoom.getUser().getPhotoUrl();
        this.recruitment_field = growRoom.getRecruitment().getField();
        this.number = growRoom.getNumber().getNumber();
        this.period = growRoom.getPeriod().getPeriod();

        // GrowRoomCategory를 리스트화 해서 categoryDetail을 출력하는 로직 - null 예외
        List<GrowRoomCategory> growRoomCategoryList = growRoom.getGrowRoomCategoryList();

        if (!growRoomCategoryList.isEmpty()) {
            this.categoryListDetail0 = growRoomCategoryList.get(0).getCategoryDetail().getName();
        } else {
            this.categoryListDetail0 = null;
        }

        if (growRoomCategoryList.size() > 1) {
            this.categoryListDetail1 = growRoomCategoryList.get(1).getCategoryDetail().getName();
        } else {
            this.categoryListDetail1 = null;
        }

        if (growRoomCategoryList.size() > 2) {
            this.categoryListDetail2 = growRoomCategoryList.get(2).getCategoryDetail().getName();
        } else {
            this.categoryListDetail2 = null;
        }

        this.title = growRoom.getPost().getTitle();
        this.content = growRoom.getPost().getTitle();
    }
}
