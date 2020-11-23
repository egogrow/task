package com.kakaopay.task.repository;

import com.kakaopay.task.domain.AllocateMoneyDetailRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AllocateMoneyDetailRedisRepository extends CrudRepository<AllocateMoneyDetailRedis, Long> {
    public List<AllocateMoneyDetailRedis> findByRoomIdAndTokenAndReceiverId(String roomId, String token, int receiverId);
    public List<AllocateMoneyDetailRedis> findByRoomIdAndToken(String roomId, String token);
    public boolean existsByRoomIdAndTokenAndMaxMoneyYn(String roomId, String token, String maxMoneyYn);
}
