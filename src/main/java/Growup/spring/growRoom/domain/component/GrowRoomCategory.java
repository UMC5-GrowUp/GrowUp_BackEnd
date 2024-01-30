package Growup.spring.growRoom.domain.component;

import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.model.GrowRoom;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GrowRoomCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growroomId")
    private GrowRoom growRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_detailId")
    private CategoryDetail categoryDetail;
}