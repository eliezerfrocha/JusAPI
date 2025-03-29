package com.example.api_base.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.api_base.service.PdfWordConverter;

import java.io.File;
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
            // Verificar se o arquivo está vazio
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio não é permitido");
            }

            // Verificar se o formato é suportado
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !(originalFilename.toLowerCase().endsWith(".pdf") ||
                    originalFilename.toLowerCase().endsWith(".docx"))) {
                return ResponseEntity.badRequest().body("Apenas arquivos PDF e DOCX são suportados");
            }

            // Criar um diretório temporário para armazenar o arquivo
            String tempDir = System.getProperty("java.io.tmpdir");
            String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
            Path tempFilePath = Path.of(tempDir, uniqueFileName);

            // Salvar o arquivo temporariamente
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            // Converter o arquivo para JSON
            String jsonResult = pdfWordConverter.convertToJson(tempFilePath.toString());

            // Limpar o arquivo temporário
            Files.deleteIfExists(tempFilePath);

            // Retornar o resultado como JSON
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

    @PostMapping("/batch-convert")
    public ResponseEntity<?> batchConvertDocuments(@RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return ResponseEntity.badRequest().body("Nenhum arquivo foi enviado");
        }

        Map<String, Object> results = new HashMap<>();
        for (MultipartFile file : files) {
            try {
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename != null && (originalFilename.toLowerCase().endsWith(".pdf") ||
                            originalFilename.toLowerCase().endsWith(".docx"))) {

                        // Processar arquivo
                        String tempDir = System.getProperty("java.io.tmpdir");
                        String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
                        Path tempFilePath = Path.of(tempDir, uniqueFileName);

                        Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

                        String jsonResult = pdfWordConverter.convertToJson(tempFilePath.toString());
                        results.put(originalFilename, jsonResult);

                        Files.deleteIfExists(tempFilePath);
                    } else {
                        results.put(originalFilename != null ? originalFilename : "unknown",
                                "Formato não suportado. Apenas PDF e DOCX são aceitos.");
                    }
                } else {
                    results.put("empty_file", "Arquivo vazio ignorado");
                }
            } catch (IOException e) {
                results.put(file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown",
                        "Erro: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(results);
    }

    @GetMapping("/formats")
    public ResponseEntity<?> getSupportedFormats() {
        Map<String, Object> formats = new HashMap<>();
        formats.put("formatos_suportados", new String[]{"pdf", "docx"});
        formats.put("descricao", "API para conversão de documentos PDF e Word para JSON");

        return ResponseEntity.ok(formats);
    }
}