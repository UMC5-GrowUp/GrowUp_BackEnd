package Growup.spring.growRoom.service;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;

import java.util.List;

public interface GrowRoomCategoryService {
    List<GrowRoomCategory> save(GrowRoom growRoom, List<CategoryDetail> categoryDetails);
}
