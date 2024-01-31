package Growup.spring.growRoom.model.component;

import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.model.GrowRoom;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GrowRoomCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "growroomId")
    private GrowRoom growRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_detailId")
    private CategoryDetail categoryDetail;
}