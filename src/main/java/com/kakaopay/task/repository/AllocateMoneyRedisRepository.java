package com.kakaopay.task.repository;

import com.kakaopay.task.domain.AllocateMoney;
import org.springframework.data.repository.CrudRepository;

public interface AllocateMoneyRedisRepository extends CrudRepository<AllocateMoney, Long> {
}
