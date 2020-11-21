package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash("AllocateMoneyDetail")
public class AllocateMoneyDetailRedis {
    @Id
    private Long _id;
    private String token;
    private String roomId;
    private int receiverId;
    private int splitMoney;
    private String maxMoneyYn;
    private LocalDateTime regDate;

    @Builder
    public AllocateMoneyDetailRedis(String token, String roomId, int receiverId, int splitMoney, String maxMoneyYn, LocalDateTime regDate) {
        this.token = token;
        this.roomId = roomId;
        this.receiverId = receiverId;
        this.splitMoney = splitMoney;
        this.maxMoneyYn = maxMoneyYn;
        this.regDate = regDate;
    }
}
