package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@RedisHash("AllocateMoneyRedis")
public class AllocateMoneyRedis {
    @Id
    private Long id;
    private String token;
    private String roomId;
    private int userId;
    private int money;
    private int count;
    private LocalDateTime regDate;

    @Builder
    public AllocateMoneyRedis(Long id, String token, String roomId, int userId, int money, int count, LocalDateTime regDate) {
        this.id = id;
        this.token = token;
        this.roomId = roomId;
        this.userId = userId;
        this.money = money;
        this.count = count;
        this.regDate = regDate;
    }

}