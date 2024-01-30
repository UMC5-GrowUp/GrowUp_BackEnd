package Growup.spring.domain;



import Growup.spring.constant.entity.BaseEntity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Participate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private  Integer liked;

    @Column(nullable = false, length = 40)
    private  Integer is_bestup;

    @Column(nullable = false, length = 40)
    private  Integer is_50up;

    @Column(nullable = false, length = 40)
    private  String status;

    @OneToOne
    @JoinColumn(name = "participatetimeId")
    private ParticipateTime participateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growroomId")
    private GrowRoom growRoom;


}