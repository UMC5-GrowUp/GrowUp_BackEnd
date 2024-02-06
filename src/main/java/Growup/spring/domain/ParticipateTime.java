package Growup.spring.domain;



import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.participate.model.Participate;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class ParticipateTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToMany(mappedBy = "participateTime", cascade = CascadeType.ALL)
    private List<Participate> participateList = new ArrayList<>();


}