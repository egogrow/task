package com.kakaopay.task.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class AllocateMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private String token;
    private String roomId;
    private int userId;
    private int money;
    private int count;
    private LocalDateTime regDate;
}
