package com.example.api_base.controller;

import com.example.api_base.API.ApiResponse;
import com.example.api_base.model.Email;
import com.example.api_base.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Criar um Email
    @PostMapping
    public ResponseEntity<ApiResponse<Email>> createEmail(@RequestBody Email email) {
        try {
            Email savedEmail = emailService.saveEmail(email);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created("Success!", savedEmail));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Error creating the email."));
        }
    }

    // Listar todos os Emails
    @GetMapping
    public ResponseEntity<ApiResponse<List<Email>>> getAllEmails() {
        try {
            List<Email> emails = emailService.getAllEmails();
            return ResponseEntity.ok(ApiResponse.success("Success!", emails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Error fetching emails."));
        }
    }

    // Buscar Email por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Email>> getEmail(@PathVariable UUID id) {
        try {
            Email email = emailService.getEmailById(id);
            return ResponseEntity.ok(ApiResponse.success("Success!", email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while fetching email."));
        }
    }

    // Atualizar Email
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Email>> updateEmail(@PathVariable UUID id, @RequestBody Email email) {
        try {
            Email updatedEmail = emailService.updateEmail(id, email);
            return ResponseEntity.ok(ApiResponse.success("Success!", updatedEmail));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while updating email."));
        }
    }

    // Deletar Email
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmail(@PathVariable UUID id) {
        try {
            emailService.deleteEmail(id);
            return ResponseEntity.ok(ApiResponse.success("Success!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while deleting email."));
        }
    }
}

