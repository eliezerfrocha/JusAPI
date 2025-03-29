package com.example.api_base.service;

import com.example.api_base.model.User;
import com.example.api_base.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Criar um usuário
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User already registered!");
        }
        return userRepository.save(user);
    }

    // Listar todos os usuários
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Buscar usuário por ID
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() 
            -> new IllegalArgumentException("User not found."));
    }

    // Atualizar usuário
    public User updateUser(UUID id, User user) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found for update.");
        }
        user.setId(id); // Garantir que o ID seja o correto para atualização
        return userRepository.save(user);
    }

    // Deletar usuário
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found for deletion.");
        }
        userRepository.deleteById(id);
    }
}
