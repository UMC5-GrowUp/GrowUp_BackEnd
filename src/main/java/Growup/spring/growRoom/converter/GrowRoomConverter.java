package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import Growup.spring.growRoom.repository.CategoryDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class GrowRoomConverter {

    private final CategoryDetailRepository categoryDetailRepository;

    public GrowRoomCategory convertToGrowRoomCategory(GrowRoom growRoom, CategoryDetail categoryDetail) {
        return GrowRoomCategory.builder()
                .growRoom(growRoom)
                .categoryDetail(categoryDetail)
                .build();
    }

    public List<CategoryDetail> convertToCategoryDetails(List<Long> categoryDetailIds) {
        List<CategoryDetail> categoryDetails = categoryDetailRepository.findAllById(categoryDetailIds);

        return categoryDetails;
    }

    public Post convertToPost(String title, String content){
        return Post.builder()
                .title(title)
                .content(content)
                .status(0)
                .build();
    }

    public static GrowRoomDtoRes.postinquiryRes inquirypost(Post post){
        return GrowRoomDtoRes.postinquiryRes.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getGrowRoom().getGrowRoomCategoryList().get(0).getCategoryDetail().getName())
                .photoUrl(post.getGrowRoom().getUser().getPhotoUrl())
                .build();
    }

    public static GrowRoomDtoRes.growRoominquiryRes growRoomDto (GrowRoom growRoom  ){ //서비스단에서 설정한 likecount를 가져오게함 controller를 통해
        return GrowRoomDtoRes.growRoominquiryRes.builder()
                .upliked(growRoom.getLikeList().size())
                .liked(!(growRoom.getLikeList().isEmpty()))
                .finaldate(growRoom.getPeriod().getPeriod())
                .view(growRoom.getView())
                .title(growRoom.getPost().getTitle())
                .build();
    }

    public static GrowRoomDtoRes.orderBy orderByDto (List<GrowRoom> growRoomList) { // 여기서 growRoomDto를 불렀기에 여기서 likecount를 넘겨 줘야함

        // 다른 converter를 가져오는 법
        List<GrowRoomDtoRes.growRoominquiryRes> growRoominquiryResList = growRoomList.stream()
                .map(GrowRoomConverter::growRoomDto)
                .collect(Collectors.toList());

        //다른 converter 참조해서 참여자 리스트를 가져옴
        return GrowRoomDtoRes.orderBy.builder()
                .growRoominquiryResList(growRoominquiryResList)
                .build();
    }
}
