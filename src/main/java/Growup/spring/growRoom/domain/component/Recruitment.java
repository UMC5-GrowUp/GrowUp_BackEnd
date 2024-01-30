package Growup.spring.growRoom.domain.component;

import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.model.GrowRoom;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL)
    private List<GrowRoom> growRoomList = new ArrayList<>();

}