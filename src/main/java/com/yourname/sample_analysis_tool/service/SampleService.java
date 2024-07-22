package com.yourname.sample_analysis_tool.service;

import com.yourname.sample_analysis_tool.model.Sample;
import com.yourname.sample_analysis_tool.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    private static final Logger logger = Logger.getLogger(SampleService.class.getName());

    public String addSample(String sampleId, String status) {
        if (sampleRepository.existsById(sampleId)) {
            logger.info("Sample already exists: " + sampleId);
            return "Sample already exists";
        }
        Sample sample = new Sample(sampleId, status, LocalDateTime.now());
        sampleRepository.save(sample);
        logger.info("Sample added successfully: " + sampleId);
        return "Sample added successfully";
    }

    public String updateSampleStatus(String sampleId, String status) {
        Sample sample = sampleRepository.findById(sampleId).orElse(null);
        if (sample == null) {
            logger.info("Sample not found: " + sampleId);
            return "Sample not found";
        }

        // Validation logic
        switch (status) {
            case "Analysis In Progress":
                if (!"Awaiting Analysis".equals(sample.getStatus())) {
                    logger.info("Sample must be 'Awaiting Analysis' to start analysis: " + sampleId);
                    return "Sample must be 'Awaiting Analysis' to start analysis";
                }
                sample.setReceivedTime(LocalDateTime.now());
                break;
            case "Completed":
                if (!"Analysis In Progress".equals(sample.getStatus())) {
                    logger.info("Sample must be 'In Progress' to complete: " + sampleId);
                    return "Sample must be 'In Progress' to complete";
                }
                sample.setCompletedTime(LocalDateTime.now());
                break;
            default:
                return "Invalid status";
        }

        sample.setStatus(status);
        sampleRepository.save(sample);
        logger.info("Sample status updated to " + status + ": " + sampleId);
        return "Sample status updated to " + status;
    }

    public List<Sample> getAllSamples() {
        List<Sample> samples = sampleRepository.findAll();
        logger.info("Retrieved all samples: " + samples.size());
        return samples;
    }

    public List<Sample> getAllInProgressSamples() {
        List<Sample> samples = sampleRepository.findAll().stream()
                .filter(sample -> "Analysis In Progress".equals(sample.getStatus()) || "Awaiting Analysis".equals(sample.getStatus()))
                .collect(Collectors.toList());
        logger.info("Retrieved in-progress and awaiting analysis samples: " + samples.size());
        return samples;
    }

    public List<Sample> getAllCompletedSamples() {
        List<Sample> samples = sampleRepository.findByStatus("Completed");
        logger.info("Retrieved completed samples: " + samples.size());
        return samples;
    }

    public Optional<Sample> findBySampleId(String sampleId) {
        Optional<Sample> sample = sampleRepository.findById(sampleId);
        logger.info("Sample " + (sample.isPresent() ? "found: " + sampleId : "not found: " + sampleId));
        return sample;
    }

    public void saveSample(Sample sample) {
        sampleRepository.save(sample);
        logger.info("Sample saved: " + sample.getSampleId());
    }
}
