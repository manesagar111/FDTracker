package com.example.fdtracker.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.service.FixedDepositService;
import com.opensymphony.xwork2.ActionSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Component
@Data
@EqualsAndHashCode(callSuper = false)
public class FixedDepositAction extends ActionSupport {
    
    @Autowired
    private FixedDepositService service;
    
    private FixedDeposit fixedDeposit = new FixedDeposit();
    private List<FixedDeposit> fixedDeposits;
    private Long id;
    private LocalDate searchFromDate;
    private LocalDate searchToDate;
    
    public String list() {
        fixedDeposits = service.findAll();
        return SUCCESS;
    }
    
    public String add() {
        return SUCCESS;
    }
    
    public String save() {
        service.save(fixedDeposit);
        return "redirect";
    }
    
    public String delete() {
        service.deleteById(id);
        return "redirect";
    }
    
    public String search() {
        if (searchFromDate != null && searchToDate != null) {
            fixedDeposits = service.findByDateRange(searchFromDate, searchToDate);
        } else {
            fixedDeposits = service.findAll();
        }
        return SUCCESS;
    }
}