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
        return "redirect:/home";
    }
    
    @GetMapping("/dashboard")
    @ResponseBody
    public String dashboardHtml(Model model) {
        return "<h1>Dashboard</h1><p><a href='/records'>View All Records</a> | <a href='/add'>Add New FD</a></p>";
    }
    
    @GetMapping("/records")
    @ResponseBody
    public String records(Model model) {
        List<FixedDeposit> fixedDeposits = service.findAll();
        StringBuilder html = new StringBuilder("<h1>Fixed Deposit Records</h1><table border='1'><tr><th>Bank</th><th>Invested Amount</th><th>Return Amount</th><th>From Date</th><th>To Date</th><th>Actions</th></tr>");
        for(FixedDeposit fd : fixedDeposits) {
            html.append("<tr><td>").append(fd.getBankName()).append("</td><td>").append(fd.getInvestedAmount()).append("</td><td>").append(fd.getReturnAmount()).append("</td><td>").append(fd.getFromDate()).append("</td><td>").append(fd.getToDate()).append("</td><td><a href='/edit/").append(fd.getId()).append("'>Edit</a> | <a href='/delete/").append(fd.getId()).append("'>Delete</a></td></tr>");
        }
        html.append("</table><p><a href='/add'>Add New FD</a> | <a href='/home'>Home</a></p>");
        return html.toString();
    }
    
    @GetMapping("/add")
    @ResponseBody
    public String add(Model model) {
        return "<h1>Add Fixed Deposit</h1><form action='/save' method='post'>" +
               "<p>Person Name: <input name='personName' required></p>" +
               "<p>Bank Name: <input name='bankName' required></p>" +
               "<p>Account Number: <input name='accountNumber' required></p>" +
               "<p>Invested Amount: <input name='investedAmount' type='number' step='0.01' required></p>" +
               "<p>Return Amount: <input name='returnAmount' type='number' step='0.01' required></p>" +
               "<p>From Date: <input name='fromDate' type='date' required></p>" +
               "<p>To Date: <input name='toDate' type='date' required></p>" +
               "<p>Description: <input name='description'></p>" +
               "<p><button type='submit'>Save</button> <a href='/records'>Cancel</a></p>" +
               "</form>";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute FixedDeposit fixedDeposit) {
        service.save(fixedDeposit);
        return "redirect:/records";
    }
    
    @GetMapping("/edit/{id}")
    @ResponseBody
    public String edit(@PathVariable Long id, Model model) {
        FixedDeposit fd = service.findById(id);
        return "<h1>Edit Fixed Deposit</h1><form action='/save' method='post'>" +
               "<input type='hidden' name='id' value='" + fd.getId() + "'>" +
               "<p>Person Name: <input name='personName' value='" + fd.getPersonName() + "' required></p>" +
               "<p>Bank Name: <input name='bankName' value='" + fd.getBankName() + "' required></p>" +
               "<p>Account Number: <input name='accountNumber' value='" + fd.getAccountNumber() + "' required></p>" +
               "<p>Invested Amount: <input name='investedAmount' type='number' step='0.01' value='" + fd.getInvestedAmount() + "' required></p>" +
               "<p>Return Amount: <input name='returnAmount' type='number' step='0.01' value='" + fd.getReturnAmount() + "' required></p>" +
               "<p>From Date: <input name='fromDate' type='date' value='" + fd.getFromDate() + "' required></p>" +
               "<p>To Date: <input name='toDate' type='date' value='" + fd.getToDate() + "' required></p>" +
               "<p>Description: <input name='description' value='" + (fd.getDescription() != null ? fd.getDescription() : "") + "'></p>" +
               "<p><button type='submit'>Update</button> <a href='/records'>Cancel</a></p>" +
               "</form>";
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
    @ResponseBody
    public String search(@RequestParam(required = false) LocalDate searchFromDate,
                        @RequestParam(required = false) LocalDate searchToDate,
                        Model model) {
        List<FixedDeposit> deposits;
        if (searchFromDate != null && searchToDate != null) {
            deposits = service.findByDateRange(searchFromDate, searchToDate);
        } else {
            deposits = service.findAll();
        }
        StringBuilder html = new StringBuilder("<h1>Search Results</h1><table border='1'><tr><th>Bank</th><th>Invested Amount</th><th>Return Amount</th><th>From Date</th><th>To Date</th></tr>");
        for(FixedDeposit fd : deposits) {
            html.append("<tr><td>").append(fd.getBankName()).append("</td><td>").append(fd.getInvestedAmount()).append("</td><td>").append(fd.getReturnAmount()).append("</td><td>").append(fd.getFromDate()).append("</td><td>").append(fd.getToDate()).append("</td></tr>");
        }
        html.append("</table><p><a href='/records'>All Records</a></p>");
        return html.toString();
    }
    
    @GetMapping("/report")
    @ResponseBody
    public String reportForm() {
        return "<h1>Generate Report</h1><form action='/generate-report' method='post'>" +
               "<p>From Date: <input name='fromDate' type='date'></p>" +
               "<p>To Date: <input name='toDate' type='date'></p>" +
               "<p>Status: <input name='status'></p>" +
               "<p>Person Name: <input name='personName'></p>" +
               "<p><button type='submit'>Generate PDF Report</button> <a href='/home'>Home</a></p>" +
               "</form>";
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