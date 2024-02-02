package Growup.spring.participate.model;


import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.domain.ParticipateTime;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.user.model.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


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

    @Column(nullable = false,  length = 40 ,columnDefinition ="0")
    private  Integer liked;

    @Column(nullable = false, length = 40)
    private  String status;

    @ManyToOne
    @JoinColumn(name = "participatetimeId")
    private ParticipateTime participateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growroomId")
    private GrowRoom growRoom;



}