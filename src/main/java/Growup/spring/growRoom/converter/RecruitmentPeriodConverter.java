package Growup.spring.growRoom.converter;

import Growup.spring.growRoom.model.component.RecruitmentPeriod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruitmentPeriodConverter {

    public RecruitmentPeriod convertToRecruitmentPeriod(LocalDate startDate, LocalDate endDate){
        return RecruitmentPeriod.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
