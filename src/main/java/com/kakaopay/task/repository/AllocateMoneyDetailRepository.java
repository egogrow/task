package com.kakaopay.task.repository;

import com.kakaopay.task.domain.AllocateMoneyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllocateMoneyDetailRepository extends JpaRepository<AllocateMoneyDetail, Long> {
    public List<AllocateMoneyDetail> findByRoomIdAndTokenAndReceiverId(String roomId, String token, int userId);
    public List<AllocateMoneyDetail> findByRoomIdAndTokenAndReceiverIdNot(String roomId, String token, int userId);
    public boolean existsByRoomIdAndTokenAndMaxMoneyYn(String roomId, String token, String maxMoneyYn);
}
