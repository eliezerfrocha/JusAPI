package com.example.api_base.service;

import com.example.api_base.model.MetadataMail;
import com.example.api_base.repository.MetadataMailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MetadataMailService {

    private final MetadataMailRepository metadataMailRepository;

    public MetadataMailService(MetadataMailRepository metadataMailRepository) {
        this.metadataMailRepository = metadataMailRepository;
    }

    public MetadataMail saveMetadataMail(MetadataMail metadataMail) {
        if (metadataMail == null) {
            throw new IllegalArgumentException("MetadataMail cannot be null.");
        }
        if (metadataMail.getEmail() == null) {
            throw new IllegalArgumentException("Associated Email must be provided.");
        }
        return metadataMailRepository.save(metadataMail);
    }

    public List<MetadataMail> getAllMetadataMails() {
        return metadataMailRepository.findAll();
    }

    public MetadataMail getMetadataMailById(UUID id) {
        return metadataMailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MetadataMail not found."));
    }

    public MetadataMail updateMetadataMail(UUID id, MetadataMail metadataMail) {
        if (!metadataMailRepository.existsById(id)) {
            throw new IllegalArgumentException("MetadataMail not found for update.");
        }
        metadataMail.setId(id);
        return metadataMailRepository.save(metadataMail);
    }

    public void deleteMetadataMail(UUID id) {
        if (!metadataMailRepository.existsById(id)) {
            throw new IllegalArgumentException("MetadataMail not found for deletion.");
        }
        metadataMailRepository.deleteById(id);
    }
}
