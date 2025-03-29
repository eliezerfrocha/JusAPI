package com.example.api_base.service;

import com.example.api_base.model.Process;
import com.example.api_base.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessService {

    private final ProcessRepository processRepository;

    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public Process saveProcess(Process process) {
        if (process == null) {
            throw new IllegalArgumentException("Process cannot be null.");
        }
        return processRepository.save(process);
    }

    public List<Process> getAllProcesses() {
        return processRepository.findAll();
    }

    public Process getProcessById(UUID id) {
        return processRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Process not found."));
    }

    public Process updateProcess(UUID id, Process process) {
        if (!processRepository.existsById(id)) {
            throw new IllegalArgumentException("Process not found for update.");
        }
        process.setId(id);
        return processRepository.save(process);
    }

    public void deleteProcess(UUID id) {
        if (!processRepository.existsById(id)) {
            throw new IllegalArgumentException("Process not found for deletion.");
        }
        processRepository.deleteById(id);
    }
}
