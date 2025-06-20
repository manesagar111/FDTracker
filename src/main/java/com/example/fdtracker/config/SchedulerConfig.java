package com.example.fdtracker.config;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.service.EmailService;
import com.example.fdtracker.service.FixedDepositService;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerConfig {
    
    private final FixedDepositService fdService;
    private final EmailService emailService;
    
    public SchedulerConfig(FixedDepositService fdService, EmailService emailService) {
        this.fdService = fdService;
        this.emailService = emailService;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void checkMaturityOnStartup() {
        checkMaturityAndSendEmail();
    }
    
    @Scheduled(cron = "0 45 20 * * ?", zone = "Asia/Kolkata")
    public void checkMaturityAndSendEmail() {
        System.out.println("Scheduled task running at: " + java.time.LocalDateTime.now());
        List<FixedDeposit> maturedDeposits = fdService.findMaturityRecords();
        System.out.println("maturedDeposits: " + maturedDeposits);
        
        if (!maturedDeposits.isEmpty()) {
            emailService.sendMaturityNotification(maturedDeposits);
            System.out.println("Email sent for " + maturedDeposits.size() + " matured deposits");
        } else {
            System.out.println("No matured deposits found. Email not sent.");
        }
    }
}