package com.kakaopay.task.repository;

import com.kakaopay.task.domain.AllocateMoney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocateMoneyRepository extends JpaRepository<AllocateMoney, Long> {
//    @Query("select a from AllocateMoney a where a.roomId = :roomId and a.token = :token")
    public AllocateMoney findByRoomIdAndToken(String roomId, String token);
}
