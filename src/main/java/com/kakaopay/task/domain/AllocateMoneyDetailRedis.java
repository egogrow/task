package com.kakaopay.task.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@RedisHash("AllocateMoneyDetailRedis")
public class AllocateMoneyDetailRedis {
    @Id
    private Long id;
    @Indexed
    private String token;
    @Indexed
    private String roomId;
    @Indexed
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
