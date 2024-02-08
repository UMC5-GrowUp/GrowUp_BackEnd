package Growup.spring.growRoom.model.component;

import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.model.GrowRoom;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class RecruitmentPeriod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private LocalDate startDate;

    @Column(nullable = false, length = 40)
    private LocalDate endDate;

    @OneToOne(mappedBy = "recruitmentPeriod", cascade = CascadeType.ALL)
    private GrowRoom growRoom;

    public void update(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
}