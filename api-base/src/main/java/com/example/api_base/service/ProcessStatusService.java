package com.example.api_base.service;

import com.example.api_base.model.ProcessStatus;
import com.example.api_base.repository.ProcessStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessStatusService {

    private final ProcessStatusRepository processStatusRepository;

    public ProcessStatusService(ProcessStatusRepository processStatusRepository) {
        this.processStatusRepository = processStatusRepository;
    }

    public ProcessStatus saveProcessStatus(ProcessStatus processStatus) {
        if (processStatus == null) {
            throw new IllegalArgumentException("ProcessStatus cannot be null.");
        }
        if (processStatus.getEmail() == null) {
            throw new IllegalArgumentException("Associated Email must be provided.");
        }
        return processStatusRepository.save(processStatus);
    }

    public List<ProcessStatus> getAllProcessStatuses() {
        return processStatusRepository.findAll();
    }

    public ProcessStatus getProcessStatusById(UUID id) {
        return processStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProcessStatus not found."));
    }

    public ProcessStatus updateProcessStatus(UUID id, ProcessStatus processStatus) {
        if (!processStatusRepository.existsById(id)) {
            throw new IllegalArgumentException("ProcessStatus not found for update.");
        }
        processStatus.setId(id);
        return processStatusRepository.save(processStatus);
    }

    public void deleteProcessStatus(UUID id) {
        if (!processStatusRepository.existsById(id)) {
            throw new IllegalArgumentException("ProcessStatus not found for deletion.");
        }
        processStatusRepository.deleteById(id);
    }
}
