package Growup.spring.domain;



import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.User.model.User;
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
    private List<GrowRoomCategory> growRoomCategoryList = new ArrayList<>();

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



}