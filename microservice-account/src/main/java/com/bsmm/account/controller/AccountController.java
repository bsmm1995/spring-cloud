package com.bsmm.account.controller;

import com.bsmm.account.dto.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(List.of(
                new AccountDTO(1l, 1l, "123-1"),
                new AccountDTO(2l, 1l, "123-2"),
                new AccountDTO(3l, 2l, "123-3")));
    }
}
