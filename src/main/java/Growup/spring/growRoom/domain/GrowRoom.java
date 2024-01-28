package Growup.spring.growRoom.domain;


import Growup.spring.user.model.User;
import Growup.spring.domain.Liked;
import Growup.spring.domain.Participate;
import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.growRoom.domain.component.GrowRoomCategory;
import Growup.spring.growRoom.domain.component.Number;
import Growup.spring.growRoom.domain.component.Period;
import Growup.spring.growRoom.domain.component.Recruitment;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GrowRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =40)
    private Integer view;

    @Column(nullable = false, length = 40)
    private String status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<GrowRoomCategory> growRoomCategoryList = new ArrayList<>(3);

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<Participate> participateList = new ArrayList<>();

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "growRoom", cascade = CascadeType.ALL)
    private List<Liked> likeList = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "recruitmentId")
    private Recruitment recruitment;

    @OneToOne
    @JoinColumn(name = "numberId")
    private Number number;

    @OneToOne
    @JoinColumn(name = "periodId")
    private Period period;

    @OneToOne
    @JoinColumn(name = "postId")
    private Post post;

    //이거 맞는지 모름 post의 component
    private String title;
    private String content;

    @Builder    // 빌더 패턴으로 객체 생성
    public GrowRoom(Recruitment recruitment, Number number, Period period, String title, String content) {
        this.recruitment = recruitment;
        this.number = number;
        this.period = period;
        this.title = title;
        this.content = content;
    }

    public void update(Recruitment recruitment, Number number, Period period, String title, String content){
        this.recruitment = recruitment;
        this.number = number;
        this.period = period;
        this.title = title;
        this.content = content;
    }
}