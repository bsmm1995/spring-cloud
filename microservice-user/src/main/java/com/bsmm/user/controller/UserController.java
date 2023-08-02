package com.bsmm.user.controller;

import com.bsmm.user.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(List.of(
                new UserDTO(1l, "Bladimir", "Minga"),
                new UserDTO(2l, "Matías", "Minga")));
    }
}
