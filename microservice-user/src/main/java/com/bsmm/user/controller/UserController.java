package com.bsmm.user.controller;

import com.bsmm.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new UserDTO(id, "Bladimir", "Minga"));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(List.of(new UserDTO(1l, "Bladimir", "Minga"), new UserDTO(2l, "Matías", "Minga")));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserDTO dto) {
        log.info("User {}", dto);
        return ResponseEntity.ok(dto);
    }
}
