package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
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
    private LocalDateTime regDate;
    private int dataVersion;

    @Builder
    public AllocateMoneyDetail(String token, String roomId, int receiverId, int splitMoney, LocalDateTime regDate, int dataVersion) {
        this.token = token;
        this.roomId = roomId;
        this.receiverId = receiverId;
        this.splitMoney = splitMoney;
        this.regDate = regDate;
        this.dataVersion = dataVersion;
    }
}