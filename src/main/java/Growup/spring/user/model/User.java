package Growup.spring.user.model;



import Growup.spring.domain.*;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.liked.model.Liked;
import Growup.spring.participate.model.Participate;
import Growup.spring.todoList.model.TodoList;
import Growup.spring.user.model.Enum.UserRole;
import Growup.spring.user.model.Enum.UserState;
import Growup.spring.constant.entity.BaseEntity;

import Growup.spring.growRoom.model.Pin;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = true, length = 200)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'NONACTIVE'")
    public UserState status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    public UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL )
    private List<UserPrefer> userPreferList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Participate> participateList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GrowRoom> growRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Liked> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TodoList> todoLists = new ArrayList<>();


    @PreUpdate
    public void preUpdate() {
        // WITHDRAW 상태에서만 실행
        if (UserState.WITHDRAW.equals(this.status)) {
            this.createdAt = null; // 또는 다른 원하는 값으로 설정 가능
            this.updatedAt = null;
            this.setNickName(null);
            this.setName("탈퇴계정");
            this.setPassword("");
            this.setNickName("");
        }
    }


}