package com.example.fdtracker.config;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.service.EmailService;
import com.example.fdtracker.service.FixedDepositService;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SchedulerConfig {
    
    private final FixedDepositService fdService;
    private final EmailService emailService;
    private LocalDateTime lastActivity = LocalDateTime.now();
    
    public SchedulerConfig(FixedDepositService fdService, EmailService emailService) {
        this.fdService = fdService;
        this.emailService = emailService;
    }
    
    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void checkMaturityOnStartup() {
        checkMaturityAndSendEmail();
    }
    
    @Scheduled(cron = "0 */05 * * * ?", zone = "Asia/Kolkata")
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
    
    // Check for idle time every minute and shutdown if idle for 5+ minutes
    @Scheduled(fixedRate = 60000) // Check every minute
    public void checkIdleAndShutdown() {
        LocalDateTime now = LocalDateTime.now();
        long idleMinutes = java.time.Duration.between(lastActivity, now).toMinutes();
        
        if (idleMinutes >= 5) {
            System.out.println("Service idle for " + idleMinutes + " minutes. Shutting down at: " + now);
            System.exit(0);
        }
    }
}