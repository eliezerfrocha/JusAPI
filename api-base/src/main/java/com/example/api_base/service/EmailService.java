package com.example.api_base.service;

import com.example.api_base.model.Email;
import com.example.api_base.repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Email saveEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null.");
        }
        return emailRepository.save(email);
    }

    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public Email getEmailById(UUID id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email not found."));
    }

    public Email updateEmail(UUID id, Email email) {
        if (!emailRepository.existsById(id)) {
            throw new IllegalArgumentException("Email not found for update.");
        }
        // Garante que o ID seja o correto para atualização
        email.setMessageId(id);
        return emailRepository.save(email);
    }

    public void deleteEmail(UUID id) {
        if (!emailRepository.existsById(id)) {
            throw new IllegalArgumentException("Email not found for deletion.");
        }
        emailRepository.deleteById(id);
    }
}
