package com.example.api_base.service;

import com.example.api_base.model.User;
import com.example.api_base.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Usuário já cadastrado!");
        }
    
        // Valida a senha
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain letters and numbers.");
        }
    
        // Criptografa a senha antes de salvar
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    
        return userRepository.save(user);
    }
    
    // Método para validar a senha
    private boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    // Listar todos os usuários
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Buscar usuário por ID
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() 
            -> new IllegalArgumentException("Usuário não encontrado"));
    }

    // Atualizar usuário
    public User updateUser(UUID id, User user) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado para atualização");
        }
        user.setId(id); // Garantir que o ID seja o correto para atualização
        return userRepository.save(user);
    }

    // Deletar usuário
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado para deleção");
        }
        userRepository.deleteById(id);
    }
}
