package Growup.spring.growRoom.domain.component;

import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.domain.GrowRoom;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Recruitment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String field;

    @OneToOne(mappedBy = "recruitment")
    private GrowRoom growRoom;
}