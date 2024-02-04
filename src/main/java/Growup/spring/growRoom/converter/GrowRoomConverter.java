package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import Growup.spring.growRoom.repository.CategoryDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


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
}
