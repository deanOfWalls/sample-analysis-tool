package com.yourname.sample_analysis_tool.controller;

import com.yourname.sample_analysis_tool.model.ChemicalSample;
import com.yourname.sample_analysis_tool.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping("/")
    public String getForm(Model model) {
        model.addAttribute("chemicals", getChemicals());
        model.addAttribute("sample", getDefaultSample());
        return "form";
    }

    @PostMapping("/generate")
    public String generateSample(@RequestParam String chemicalName, Model model) {
        model.addAttribute("chemicals", getChemicals());
        String chemicalId = getChemicalIdByName(chemicalName);
        ChemicalSample sample = sampleService.generateSample(chemicalId, chemicalName);
        model.addAttribute("sample", sample);
        return "form";
    }

    private List<String> getChemicals() {
        return List.of("Chemical 1", "Chemical 2", "Chemical 3");
    }

    private String getChemicalIdByName(String chemicalName) {
        switch (chemicalName) {
            case "Chemical 1":
                return "01";
            case "Chemical 2":
                return "02";
            case "Chemical 3":
                return "03";
            default:
                return "00";
        }
    }

    private ChemicalSample getDefaultSample() {
        ChemicalSample sample = new ChemicalSample();
        sample.setChemicalName("None");
        sample.setTime("None");
        sample.setDate("None");
        sample.setSampleId("None");
        sample.setQrCode("");
        return sample;
    }
}
