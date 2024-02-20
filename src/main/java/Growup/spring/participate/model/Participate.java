package Growup.spring.participate.model;


import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.participate.model.Enum.ParticipateStatus;
import Growup.spring.user.model.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Participate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "int default 0")
    private  Integer liked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40, columnDefinition = "varchar(40) default 'NONHEAD'")
    private ParticipateStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growroomId")
    private GrowRoom growRoom;

    @OneToMany(mappedBy = "participate", cascade = CascadeType.ALL)
    private List<ParticipateTime> participateTimeList = new ArrayList<>();



}