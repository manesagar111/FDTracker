package com.example.fdtracker.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.service.EmailService;

@RestController
public class TestController {
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/test-email")
    public String testEmail() {
        try {
            FixedDeposit testFd = new FixedDeposit();
            testFd.setBankName("Test Bank");
            testFd.setAccountNumber("123456789");
            testFd.setReturnAmount(BigDecimal.valueOf(50000));
            
            List<FixedDeposit> testList = Arrays.asList(testFd);
            emailService.sendMaturityNotification(testList);
            
            return "Email notification logged to console! Check application logs.";
        } catch (Exception e) {
            return "Email failed: " + e.getMessage();
        }
    }
}