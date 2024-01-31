package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import org.springframework.stereotype.Component;

@Component
public class GrowRoomCategoryConverter {

    public GrowRoomCategory convertToGrowRoomCategory(GrowRoom growRoom, CategoryDetail categoryDetail) {
        return GrowRoomCategory.builder()
                .growRoom(growRoom)
                .categoryDetail(categoryDetail)
                .build();
    }
}

