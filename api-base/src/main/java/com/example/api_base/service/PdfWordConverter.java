package com.example.api_base.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
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
            docMap.put("tipo", "pdf");
            docMap.put("nome", file.getName());
            docMap.put("paginas", document.getNumberOfPages());
            String content = stripper.getText(document);
            docMap.put("conteudo", content);
            if (document.getNumberOfPages() > 1) {
                List<String> pageContents = new ArrayList<>();
                for (int i = 1; i <= document.getNumberOfPages(); i++) {
                    stripper.setStartPage(i);
                    stripper.setEndPage(i);
                    pageContents.add(stripper.getText(document));
                }
                docMap.put("paginas_conteudo", pageContents);
            }
        }

        return docMap;
    }
    private Map<String, Object> convertDocxToMap(File file) throws IOException {
        Map<String, Object> docMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            docMap.put("tipo", "docx");
            docMap.put("nome", file.getName());
            String content = extractor.getText();
            docMap.put("conteudo", content);
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