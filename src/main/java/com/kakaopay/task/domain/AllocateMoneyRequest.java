package com.kakaopay.task.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AllocateMoneyRequest {
    private int money;
    private int count;
}