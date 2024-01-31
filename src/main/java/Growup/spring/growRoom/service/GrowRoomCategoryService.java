package Growup.spring.growRoom.service;

import Growup.spring.growRoom.converter.GrowRoomCategoryConverter;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import Growup.spring.growRoom.repository.GrowRoomCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GrowRoomCategoryService {
    private final GrowRoomCategoryRepository growRoomCategoryRepository;
    private final GrowRoomCategoryConverter growRoomCategoryConverter;

    @Transactional
    public List<GrowRoomCategory> save(GrowRoom growRoom, List<CategoryDetail> categoryDetails) {
        List<GrowRoomCategory> growRoomCategories = new ArrayList<>();

        for (CategoryDetail categoryDetail : categoryDetails) {
            // GrowRoomCategoryConverter 클래스를 활용하여 변환
            GrowRoomCategory growRoomCategory = growRoomCategoryConverter.convertToGrowRoomCategory(growRoom, categoryDetail);

            growRoomCategories.add(growRoomCategory);
        }

        return growRoomCategoryRepository.saveAll(growRoomCategories);
    }
}
