package com.yourname.sample_analysis_tool.repository;

import com.yourname.sample_analysis_tool.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SampleRepository extends JpaRepository<Sample, String> {
    List<Sample> findByStatus(String status);
}
