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
public class AllocateMoneyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _id;
    private String token;
    private String roomId;
    private int receiverId;
    private int splitMoney;
    private String maxMoneyYn;
    private LocalDateTime regDate;

    @Builder
    public AllocateMoneyDetail(String token, String roomId, int receiverId, int splitMoney, String maxMoneyYn, LocalDateTime regDate) {
        this.token = token;
        this.roomId = roomId;
        this.receiverId = receiverId;
        this.splitMoney = splitMoney;
        this.maxMoneyYn = maxMoneyYn;
        this.regDate = regDate;
    }
}