package com.example.api_base.controller;

import com.example.api_base.model.User;
import com.example.api_base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> listUser() {
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable UUID id) {
        return userService.findUserById(id);
    }

    @PostMapping
    public User salvarUsuario(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
