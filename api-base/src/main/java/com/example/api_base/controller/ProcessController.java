package com.example.api_base.controller;

import com.example.api_base.API.ApiResponse;
import com.example.api_base.model.Process;
import com.example.api_base.service.ProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/processes")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    // Criar um Process
    @PostMapping
    public ResponseEntity<ApiResponse<Process>> createProcess(@RequestBody Process process) {
        try {
            Process savedProcess = processService.saveProcess(process);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created("Success!", savedProcess));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Error creating the process."));
        }
    }

    // Listar todos os Processes
    @GetMapping
    public ResponseEntity<ApiResponse<List<Process>>> getAllProcesses() {
        try {
            List<Process> processes = processService.getAllProcesses();
            return ResponseEntity.ok(ApiResponse.success("Success!", processes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Error fetching processes."));
        }
    }

    // Buscar Process por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Process>> getProcess(@PathVariable UUID id) {
        try {
            Process process = processService.getProcessById(id);
            return ResponseEntity.ok(ApiResponse.success("Success!", process));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while fetching process."));
        }
    }

    // Atualizar Process
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Process>> updateProcess(@PathVariable UUID id, @RequestBody Process process) {
        try {
            Process updatedProcess = processService.updateProcess(id, process);
            return ResponseEntity.ok(ApiResponse.success("Success!", updatedProcess));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while updating process."));
        }
    }

    // Deletar Process
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProcess(@PathVariable UUID id) {
        try {
            processService.deleteProcess(id);
            return ResponseEntity.ok(ApiResponse.success("Success!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalServerError("Internal error while deleting process."));
        }
    }
}
