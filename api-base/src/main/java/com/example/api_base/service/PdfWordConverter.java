package com.example.api_base.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PdfWordConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public String convertToJson(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Arquivo não encontrado: " + filePath);
        }

        String fileName = file.getName().toLowerCase();
        Map<String, Object> result = new HashMap<>();

        if (fileName.endsWith(".pdf")) {
            result = convertPdfToMap(file);
        } else if (fileName.endsWith(".docx")) {
            result = convertDocxToMap(file);
        } else {
            throw new IOException("Formato de arquivo não suportado. Use PDF (.pdf) ou Word moderno (.docx)");
        }

        return mapper.writeValueAsString(result);
    }

    private Map<String, Object> convertPdfToMap(File file) throws IOException {
        Map<String, Object> docMap = new HashMap<>();

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            stripper.setParagraphEnd("\n\n");
            stripper.setSortByPosition(true);  // Importante para PDFs com múltiplas colunas
            stripper.setAddMoreFormatting(true);  // Melhora a formatação

            // Informações básicas
            docMap.put("tipo", "pdf");
            docMap.put("nome", file.getName());
            docMap.put("paginas", document.getNumberOfPages());

            // Extrai todo o conteúdo
            String fullContent = stripper.getText(document);
            docMap.put("conteudo", fullContent.trim());

            // Extrai por páginas e lista palavras
            if (document.getNumberOfPages() > 1) {
                List<String> pageContents = new ArrayList<>();
                List<List<String>> pageWords = new ArrayList<>();

                for (int i = 1; i <= document.getNumberOfPages(); i++) {
                    stripper.setStartPage(i);
                    stripper.setEndPage(i);
                    String pageText = stripper.getText(document).trim();
                    pageContents.add(pageText);

                    // Extrai palavras da página
                    List<String> words = extractWordsFromText(pageText);
                    pageWords.add(words);
                }

                docMap.put("paginas_conteudo", pageContents);
                docMap.put("palavras_por_pagina", pageWords);  // Nova lista de palavras
            } else {
                // Extrai palavras para PDF de uma página
                List<String> words = extractWordsFromText(fullContent);
                docMap.put("palavras", words);
            }
        }

        return docMap;
    }

    private List<String> extractWordsFromText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        // Remove pontuação e divide em palavras
        return Arrays.stream(text.split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚãõâêîôûàèìòùçÇ]", ""))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertDocxToMap(File file) throws IOException {
        Map<String, Object> docMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            docMap.put("tipo", "docx");
            docMap.put("nome", file.getName());

            // Extraindo o conteúdo do documento
            String content = extractor.getText();
            docMap.put("conteudo", content);

            // Adicionando metadados, se disponíveis
            if (document.getProperties() != null && document.getProperties().getCoreProperties() != null) {
                Map<String, Object> metadados = new HashMap<>();
                if (document.getProperties().getCoreProperties().getCreator() != null) {
                    metadados.put("autor", document.getProperties().getCoreProperties().getCreator());
                }
                if (document.getProperties().getCoreProperties().getTitle() != null) {
                    metadados.put("titulo", document.getProperties().getCoreProperties().getTitle());
                }
                if (!metadados.isEmpty()) {
                    docMap.put("metadados", metadados);
                }
            }
        }

        return docMap;
    }
}
