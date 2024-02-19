package Growup.spring.growRoom.service;

import Growup.spring.growRoom.converter.GrowRoomConverter;
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
public class GrowRoomCategoryServiceImpl implements GrowRoomCategoryService {

    private final GrowRoomCategoryRepository growRoomCategoryRepository;
    private final GrowRoomConverter growRoomConverter;

    @Transactional
    public List<GrowRoomCategory> save(GrowRoom growRoom, List<CategoryDetail> categoryDetails) {
        List<GrowRoomCategory> growRoomCategories = new ArrayList<>();

        for (CategoryDetail categoryDetail : categoryDetails) {
            // GrowRoomConverter 클래스를 활용하여 변환
            GrowRoomCategory growRoomCategory = growRoomConverter.convertToGrowRoomCategory(growRoom, categoryDetail);

            growRoomCategories.add(growRoomCategory);
        }

        return growRoomCategoryRepository.saveAll(growRoomCategories);
    }
}
