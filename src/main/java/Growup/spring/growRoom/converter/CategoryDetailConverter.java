package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.repository.CategoryDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryDetailConverter {

    private final CategoryDetailRepository categoryDetailRepository;

    public List<CategoryDetail> convertToCategoryDetails(List<Long> categoryDetailIds) {

        List<CategoryDetail> categoryDetails = categoryDetailRepository.findAllById(categoryDetailIds);

        return categoryDetails;
    }
}
