package com.kakaopay.task.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllocateMoneyRequest {
    private int money;
    private int count;
}