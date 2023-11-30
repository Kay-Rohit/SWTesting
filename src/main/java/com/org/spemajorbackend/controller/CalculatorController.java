package com.org.spemajorbackend.controller;

import com.org.spemajorbackend.dro.ConversionRequest;
import com.org.spemajorbackend.dro.BMI;
import com.org.spemajorbackend.service.CalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
@RestController
@RequestMapping("/calculate")
@Slf4j
public class CalculatorController {
    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }
    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    @GetMapping("/bmi")
    public ResponseEntity<?> calculateBMI(@RequestBody BMI bmi) {
        try {
            Double myBMI = calculatorService.calculateBMI(bmi.getHeight(), bmi.getWeight());
            return ResponseEntity.ok(myBMI);
        }
        catch (Exception e) {
            logger.info("Exception caught: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
