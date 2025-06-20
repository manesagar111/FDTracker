package com.example.fdtracker.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fixed_deposits")
public class FixedDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String personName;
    
    @Column(nullable = false)
    private String bankName;
    
    @Column(nullable = false)
    private String accountNumber;
    
    @Column(nullable = false)
    private LocalDate fromDate;
    
    @Column(nullable = false)
    private LocalDate toDate;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal investedAmount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal returnAmount;
    
    @Column(length = 500, columnDefinition = "VARCHAR(500)")
    private String description;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean mailSent = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;
    
    @Column
    private LocalDate lastNotificationDate;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean autoRenewal = false;
    
    @Column
    private Integer renewalCount = 0;
    
    @Column
    private LocalDate lastRenewalDate;
    
    public FixedDeposit() {}
    
    public FixedDeposit(Long id, String personName, String bankName, String accountNumber, LocalDate fromDate, LocalDate toDate, BigDecimal investedAmount, BigDecimal returnAmount, String description) {
        this.id = id;
        this.personName = personName;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.investedAmount = investedAmount;
        this.returnAmount = returnAmount;
        this.description = description;
        this.status = Status.NEW;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    
    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
    
    public BigDecimal getInvestedAmount() { return investedAmount; }
    public void setInvestedAmount(BigDecimal investedAmount) { this.investedAmount = investedAmount; }
    
    public BigDecimal getReturnAmount() { return returnAmount; }
    public void setReturnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getMailSent() { return mailSent; }
    public void setMailSent(Boolean mailSent) { this.mailSent = mailSent; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public LocalDate getLastNotificationDate() { return lastNotificationDate; }
    public void setLastNotificationDate(LocalDate lastNotificationDate) { this.lastNotificationDate = lastNotificationDate; }
    
    public Boolean getAutoRenewal() { return autoRenewal; }
    public void setAutoRenewal(Boolean autoRenewal) { this.autoRenewal = autoRenewal; }
    
    public Integer getRenewalCount() { return renewalCount; }
    public void setRenewalCount(Integer renewalCount) { this.renewalCount = renewalCount; }
    
    public LocalDate getLastRenewalDate() { return lastRenewalDate; }
    public void setLastRenewalDate(LocalDate lastRenewalDate) { this.lastRenewalDate = lastRenewalDate; }
    
    public String getMaskedAccountNumber() {
        if (accountNumber == null || accountNumber.length() <= 4) return accountNumber;
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}