package com.kakaopay.task.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(indexes = { @Index(name = "IDX_ALLOCATE", unique = false, columnList = "room_id, token") })
public class Allocate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long _id;

    @Column(columnDefinition = "varchar(3) default '' ", nullable = false)
    private String token;

    @Column(name = "room_id", nullable = false, length = 50)
    private String roomId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "target_amount")
    private long targetAmount;

    @Column(name = "terget_number")
    private int tergetNumber;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;
}
