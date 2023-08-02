package com.bsmm.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private Long userId;
    private String number;
}
