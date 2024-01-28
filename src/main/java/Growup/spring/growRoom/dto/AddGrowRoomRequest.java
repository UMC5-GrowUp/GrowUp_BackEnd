package Growup.spring.growRoom.dto;

import Growup.spring.user.model.User;
import Growup.spring.growRoom.domain.GrowRoom;
import Growup.spring.growRoom.domain.Post;
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
public class AddGrowRoomRequest {

    // user
    private User user;

    // growroom
    private Recruitment recruitment; //모집 구분
    private Number number;  //모집 인원
    //모집 기간 X
    private Period period;  //진행기간

    // category
//    private String category;    // 임시 코드
    private List<GrowRoomCategory> categoryList;    //카테고리 리스트(여러개)

    // post
    private String title;
    private String content;

    public GrowRoom toEntity(){
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();

        GrowRoom growRoom = GrowRoom.builder()
                .user(user)
                .recruitment(recruitment)
                .number(number)
                .period(period)
                .growRoomCategoryList(categoryList)
                .post(post)
                .build();

        post.setGrowRoom(growRoom);

        return growRoom;
    }
}
