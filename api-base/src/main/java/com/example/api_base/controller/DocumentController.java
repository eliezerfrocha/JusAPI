package com.example.api_base.controller;
import com.example.api_base.service.PdfWordConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final PdfWordConverter pdfWordConverter;

    public DocumentController(PdfWordConverter pdfWordConverter) {
        this.pdfWordConverter = pdfWordConverter;
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convertDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio não é permitido");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !(originalFilename.toLowerCase().endsWith(".pdf") ||
                    originalFilename.toLowerCase().endsWith(".docx"))) {
                return ResponseEntity.badRequest().body("Apenas arquivos PDF e DOCX são suportados");
            }

            String tempDir = System.getProperty("java.io.tmpdir");
            String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
            Path tempFilePath = Path.of(tempDir, uniqueFileName);

            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            String jsonResult = pdfWordConverter.convertToJson(tempFilePath.toString());

            Files.deleteIfExists(tempFilePath);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResult);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Falha ao processar o arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
