package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
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

    @Builder
    public AllocateMoney(String token, String roomId, int userId, int money, int count, LocalDateTime regDate) {
        this.token = token;
        this.roomId = roomId;
        this.userId = userId;
        this.money = money;
        this.count = count;
        this.regDate = regDate;
    }
}
