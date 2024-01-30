package Growup.spring.growRoom.domain;

import Growup.spring.constant.entity.BaseEntity;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false, length = 40)
    private String status;

    @OneToOne(mappedBy = "post")
    private GrowRoom growRoom;

    @Builder    // 빌더 패턴으로 객체 생성
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setGrowRoom(GrowRoom growRoom) {
        this.growRoom = growRoom;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}