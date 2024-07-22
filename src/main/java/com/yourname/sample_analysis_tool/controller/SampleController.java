package com.yourname.sample_analysis_tool.controller;

import com.yourname.sample_analysis_tool.model.Sample;
import com.yourname.sample_analysis_tool.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping("/drop")
    public String showDropPage(Model model) {
        return "drop";
    }

    @GetMapping("/receive")
    public String showReceivePage(Model model) {
        return "receive";
    }

    @GetMapping("/complete")
    public String showCompletePage(Model model) {
        return "complete";
    }

    @GetMapping("/queue")
    public String showQueuePage(Model model) {
        List<Sample> samples = sampleService.getAllSamples();
        model.addAttribute("samples", samples);
        return "queue";
    }

    @PostMapping("/api/dropOff")
    @ResponseBody
    public String dropOffSample(@RequestBody Sample sample) {
        return sampleService.addSample(sample.getSampleId(), "Awaiting Analysis");
    }

    @PostMapping("/api/receive")
    @ResponseBody
    public String receiveSample(@RequestBody Sample sample) {
        return sampleService.updateSampleStatus(sample.getSampleId(), "Analysis In Progress");
    }

    @PostMapping("/api/complete")
    @ResponseBody
    public String completeSample(@RequestBody Sample sample) {
        return sampleService.updateSampleStatus(sample.getSampleId(), "Completed");
    }
}
