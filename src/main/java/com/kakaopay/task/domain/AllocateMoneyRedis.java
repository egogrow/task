package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash("AllocateMoney")
public class AllocateMoneyRedis {
    @Id
    private Long _id;
    private String token;
    private String roomId;
    private int userId;
    private int money;
    private int count;
    private LocalDateTime regDate;

    @Builder
    public AllocateMoneyRedis(String token, String roomId, int userId, int money, int count, LocalDateTime regDate) {
        this.token = token;
        this.roomId = roomId;
        this.userId = userId;
        this.money = money;
        this.count = count;
        this.regDate = regDate;
    }
}