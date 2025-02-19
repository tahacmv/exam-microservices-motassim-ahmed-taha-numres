package com.amotassim.api_gateway.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/patientService")
    public String patientServiceFallback() {
        return "Patient Service is currently unavailable. Please try again later.";
    }

    @GetMapping("/practitionerService")
    public String practitionerServiceFallback() {
        return "Practitioner Service is currently unavailable. Please try again later.";
    }
}