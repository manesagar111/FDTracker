package com.example.fdtracker.service;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.model.Status;
import com.example.fdtracker.repository.FixedDepositRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FixedDepositService {
    
    private final FixedDepositRepository repository;
    
    public FixedDepositService(FixedDepositRepository repository) {
        this.repository = repository;
    }
    
    public FixedDeposit save(FixedDeposit fixedDeposit) {
        if (fixedDeposit.getId() != null) {
            FixedDeposit existing = repository.findById(fixedDeposit.getId()).orElse(null);
            if (existing != null) {
                fixedDeposit.setMailSent(existing.getMailSent());
            }
        }
        if (fixedDeposit.getAccountNumber() != null) {
            fixedDeposit.setAccountNumber(maskAccountNumber(fixedDeposit.getAccountNumber()));
        }
        return repository.save(fixedDeposit);
    }
    
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) return accountNumber;
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
    
    public List<FixedDeposit> findAll() {
        return repository.findAll();
    }
    
    public List<FixedDeposit> findByDateRange(LocalDate fromDate, LocalDate toDate) {
        return repository.findByDateRange(fromDate, toDate);
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public FixedDeposit findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<FixedDeposit> findMaturityRecords() {
        LocalDate fromDate = LocalDate.now().minusMonths(1);
        LocalDate toDate = LocalDate.now().plusMonths(1);
        List<FixedDeposit> maturityRecords = repository.findMaturityInRange(fromDate, toDate);
        
        // Add reminder records (7 days since last notification)
        LocalDate reminderDate = LocalDate.now().minusDays(7);
        List<FixedDeposit> reminderRecords = repository.findPendingReminders(reminderDate);
        
        maturityRecords.addAll(reminderRecords);
        return maturityRecords;
    }
    
    public Map<String, Double> getBankWiseInvestments() {
        List<FixedDeposit> allFDs = repository.findAll();
        return allFDs.stream()
            .collect(Collectors.groupingBy(
                FixedDeposit::getBankName,
                Collectors.summingDouble(fd -> fd.getInvestedAmount().doubleValue())
            ));
    }
    
    public void renewFd(Long id) {
        FixedDeposit fd = repository.findById(id).orElse(null);
        if (fd != null && Boolean.TRUE.equals(fd.getAutoRenewal())) {
            Integer currentCount = fd.getRenewalCount();
            fd.setRenewalCount(currentCount != null ? currentCount + 1 : 1);
            fd.setLastRenewalDate(LocalDate.now());
            LocalDate newFromDate = fd.getToDate();
            fd.setFromDate(newFromDate);
            fd.setToDate(newFromDate.plusYears(1));
            fd.setStatus(Status.RENEWED);
            fd.setMailSent(Boolean.FALSE);
            repository.save(fd);
        }
    }
}