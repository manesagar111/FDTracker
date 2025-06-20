package com.example.fdtracker.controller;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.service.FixedDepositService;
import com.example.fdtracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class FdController {
    
    @Autowired
    private FixedDepositService service;
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping("/")
    public String dashboard(Model model) {
        Map<String, Double> bankWiseData = service.getBankWiseInvestments();
        model.addAttribute("bankWiseData", bankWiseData);
        return "dashboard";
    }
    
    @GetMapping("/records")
    public String records(Model model) {
        List<FixedDeposit> fixedDeposits = service.findAll();
        model.addAttribute("fixedDeposits", fixedDeposits);
        return "records";
    }
    
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("fixedDeposit", new FixedDeposit());
        return "add";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute FixedDeposit fixedDeposit) {
        service.save(fixedDeposit);
        return "redirect:/records";
    }
    
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("fixedDeposit", service.findById(id));
        return "add";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/records";
    }
    
    @GetMapping("/renew/{id}")
    public String renewFd(@PathVariable Long id) {
        service.renewFd(id);
        return "redirect:/records";
    }
    
    @PostMapping("/search")
    public String search(@RequestParam(required = false) LocalDate searchFromDate,
                        @RequestParam(required = false) LocalDate searchToDate,
                        Model model) {
        List<FixedDeposit> deposits;
        if (searchFromDate != null && searchToDate != null) {
            deposits = service.findByDateRange(searchFromDate, searchToDate);
        } else {
            deposits = service.findAll();
        }
        model.addAttribute("fixedDeposits", deposits);
        return "list";
    }
    
    @GetMapping("/report")
    public String reportForm() {
        return "report";
    }
    
    @PostMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport(@RequestParam(required = false) LocalDate fromDate,
                                               @RequestParam(required = false) LocalDate toDate,
                                               @RequestParam(required = false) String status,
                                               @RequestParam(required = false) String personName) {
        byte[] pdfBytes = reportService.generatePdfReport(fromDate, toDate, status, personName);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "fd-report.pdf");
        
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}