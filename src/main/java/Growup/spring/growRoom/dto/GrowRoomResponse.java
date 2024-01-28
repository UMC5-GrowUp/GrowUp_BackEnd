package Growup.spring.growRoom.dto;

import Growup.spring.growRoom.domain.GrowRoom;
import lombok.Getter;


@Getter
public class GrowRoomResponse {

    // user
    private final String nick_name;
    private final String photo_url;

    // growroom
    private final String recruitment_field;
    private final Integer number;
    private final String period;

    // category - 최대 3개
    private final Long categoryListDetail1;
    private final Long categoryListDetail2;
    private final Long categoryListDetail3;

    // post
    private final String title;
    private final String content;

    public GrowRoomResponse(GrowRoom growRoom) {
        this.nick_name = growRoom.getUser().getNickName();
        this.photo_url = growRoom.getUser().getPhotoUrl();
        this.recruitment_field = growRoom.getRecruitment().getField();
        this.number = growRoom.getNumber().getNumber();
        this.period = growRoom.getPeriod().getPeriod();

        // 초기화를 제거하고, 변수를 항상 초기화하는 것을 보장
        this.categoryListDetail1 = getCategoryDetailId(growRoom, 0);
        this.categoryListDetail2 = getCategoryDetailId(growRoom, 1);
        this.categoryListDetail3 = getCategoryDetailId(growRoom, 2);

        this.title = growRoom.getPost().getTitle();
        this.content = growRoom.getPost().getTitle();
    }

    // 반복되는 로직을 재사용, 그 결과에 따라 null 값을 반환
    private Long getCategoryDetailId(GrowRoom growRoom, int index) {
        if (index < growRoom.getGrowRoomCategoryList().size()) {
            return growRoom.getGrowRoomCategoryList().get(index).getCategoryDetail().getId();
        }
        return null;
    }
}
