package Growup.spring.growRoom.model;

import Growup.spring.constant.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @Column(columnDefinition = "Integer default '0'", nullable = false, length = 40)
    private Integer status;

    @OneToOne(mappedBy = "post",cascade = CascadeType.ALL)
    private GrowRoom growRoom;

    public void setGrowRoom(GrowRoom growRoom) {
        this.growRoom = growRoom;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}