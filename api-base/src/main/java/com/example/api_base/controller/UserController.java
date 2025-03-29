package com.example.api_base.controller;

import com.example.api_base.API.ApiResponse;
import com.example.api_base.model.User;
import com.example.api_base.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Criar um usuário
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created("Success!", savedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Error creating the user."));
        }
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Success!", users));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Error fetching users."));
        }
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable UUID id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("Success!", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while fetching user."));
        }
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(ApiResponse.success("Success!", updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while updating user."));
        }
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("Success!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while deleting user."));
        }
    }
}
