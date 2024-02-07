package Growup.spring.calender.model;

import Growup.spring.calender.model.Enum.CalenderColorStatus;
import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.user.model.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CalenderColor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate day;

    @Enumerated(EnumType.STRING)
    private CalenderColorStatus color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="userId")
    private User user;


}
