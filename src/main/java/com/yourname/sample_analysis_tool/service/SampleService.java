package com.yourname.sample_analysis_tool.service;

import com.yourname.sample_analysis_tool.model.Sample;
import com.yourname.sample_analysis_tool.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    public String addSample(String sampleId, String status) {
        if (sampleRepository.existsById(sampleId)) {
            return "Sample already exists";
        }
        Sample sample = new Sample(sampleId, status, LocalDateTime.now());
        sampleRepository.save(sample);
        return "Sample added successfully";
    }

    public String updateSampleStatus(String sampleId, String status) {
        Sample sample = sampleRepository.findById(sampleId).orElse(null);
        if (sample == null) {
            return "Sample not found";
        }
        if ("Completed".equals(status) && !"Analysis In Progress".equals(sample.getStatus())) {
            return "Sample must be 'In Progress' to complete";
        }
        sample.setStatus(status);
        sampleRepository.save(sample);
        return "Sample status updated to " + status;
    }

    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

    public List<Sample> getAllCompletedSamples() {
        return sampleRepository.findAll().stream()
                .filter(sample -> "Completed".equals(sample.getStatus()))
                .collect(Collectors.toList());
    }

    public Optional<Sample> findBySampleId(String sampleId) {
        return sampleRepository.findById(sampleId);
    }

    public void saveSample(Sample sample) {
        sampleRepository.save(sample);
    }
}
