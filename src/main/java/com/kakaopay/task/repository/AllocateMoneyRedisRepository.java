package com.kakaopay.task.repository;

import com.kakaopay.task.domain.AllocateMoneyRedis;
import org.springframework.data.repository.CrudRepository;

public interface AllocateMoneyRedisRepository extends CrudRepository<AllocateMoneyRedis, Long> {
    public AllocateMoneyRedis findByRoomIdAndToken(String roomId, String Token);
}
