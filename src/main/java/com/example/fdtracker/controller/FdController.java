package com.example.fdtracker.controller;

import com.example.fdtracker.config.SchedulerConfig;
import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.service.EmailService;
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
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SchedulerConfig schedulerConfig;
    
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
        schedulerConfig.updateLastActivity();
        List<FixedDeposit> fixedDeposits = service.findAll();
        StringBuilder html = new StringBuilder("<!DOCTYPE html><html><head><title>FD Records</title>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "</head><body class='bg-light'><div class='container mt-4'>" +
               "<div class='d-flex justify-content-between align-items-center mb-4'>" +
               "<h2 class='text-primary'>Fixed Deposit Records</h2>" +
               "<div><a href='/add' class='btn btn-success me-2'>Add New FD</a>" +
               "<a href='/home' class='btn btn-secondary'>Home</a></div></div>" +
               "<div class='card shadow'><div class='card-body'>" +
               "<div class='table-responsive'><table class='table table-striped table-hover'>" +
               "<thead class='table-dark'><tr><th>Person</th><th>Bank</th><th>Invested Amount</th><th>Return Amount</th><th>From Date</th><th>To Date</th><th>Actions</th></tr></thead><tbody>");
        for(FixedDeposit fd : fixedDeposits) {
            html.append("<tr><td>").append(fd.getPersonName()).append("</td><td>").append(fd.getBankName()).append("</td><td>₹").append(fd.getInvestedAmount()).append("</td><td>₹").append(fd.getReturnAmount()).append("</td><td>").append(fd.getFromDate()).append("</td><td>").append(fd.getToDate()).append("</td><td>" +
                    "<a href='/edit/").append(fd.getId()).append("' class='btn btn-sm btn-warning me-1'>Edit</a>" +
                    "<a href='/delete/").append(fd.getId()).append("' class='btn btn-sm btn-danger me-1' onclick='return confirm(\"Are you sure?\")''>Delete</a>" +
                    "<a href='/send-mail/").append(fd.getId()).append("' class='btn btn-sm btn-info'>Send Mail</a></td></tr>");
        }
        html.append("</tbody></table></div></div></div></div></body></html>");
        return html.toString();
    }
    
    @GetMapping("/add")
    @ResponseBody
    public String add(Model model) {
        schedulerConfig.updateLastActivity();
        return "<!DOCTYPE html><html><head><title>Add FD</title>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "</head><body class='bg-light'><div class='container mt-4'>" +
               "<div class='row justify-content-center'><div class='col-md-8'>" +
               "<div class='card shadow'><div class='card-header bg-success text-white'>" +
               "<h3 class='mb-0'>Add Fixed Deposit</h3></div><div class='card-body'>" +
               "<form action='/save' method='post'>" +
               "<div class='row'><div class='col-md-6 mb-3'>" +
               "<label class='form-label'>Person Name</label>" +
               "<input name='personName' class='form-control' required></div>" +
               "<div class='col-md-6 mb-3'><label class='form-label'>Bank Name</label>" +
               "<input name='bankName' class='form-control' required></div></div>" +
               "<div class='row'><div class='col-md-6 mb-3'>" +
               "<label class='form-label'>Account Number</label>" +
               "<input name='accountNumber' class='form-control' required></div>" +
               "<div class='col-md-6 mb-3'><label class='form-label'>Description</label>" +
               "<input name='description' class='form-control'></div></div>" +
               "<div class='row'><div class='col-md-6 mb-3'>" +
               "<label class='form-label'>Invested Amount (₹)</label>" +
               "<input name='investedAmount' type='number' step='0.01' class='form-control' required></div>" +
               "<div class='col-md-6 mb-3'><label class='form-label'>Return Amount (₹)</label>" +
               "<input name='returnAmount' type='number' step='0.01' class='form-control' required></div></div>" +
               "<div class='row'><div class='col-md-6 mb-3'>" +
               "<label class='form-label'>From Date</label>" +
               "<input name='fromDate' type='date' class='form-control' required></div>" +
               "<div class='col-md-6 mb-3'><label class='form-label'>To Date</label>" +
               "<input name='toDate' type='date' class='form-control' required></div></div>" +
               "<div class='d-grid gap-2 d-md-flex justify-content-md-end'>" +
               "<button type='submit' class='btn btn-success me-2'>Save FD</button>" +
               "<a href='/records' class='btn btn-secondary'>Cancel</a></div>" +
               "</form></div></div></div></div></div></body></html>";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute FixedDeposit fixedDeposit) {
        schedulerConfig.updateLastActivity();
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
    
    @GetMapping("/send-mail/{id}")
    public String sendMail(@PathVariable Long id) {
        FixedDeposit fd = service.findById(id);
        if (fd != null) {
            try {
                emailService.sendMaturityNotification(java.util.Arrays.asList(fd));
                System.out.println("Email sent successfully for FD ID: " + id);
            } catch (Exception e) {
                System.out.println("Failed to send email for FD ID: " + id + ", Error: " + e.getMessage());
            }
        }
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
        return "<!DOCTYPE html><html><head><title>Generate Report</title>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "</head><body class='bg-light'><div class='container mt-4'>" +
               "<div class='row justify-content-center'><div class='col-md-8'>" +
               "<div class='card shadow'><div class='card-header bg-info text-white'>" +
               "<h3 class='mb-0'>Generate FD Report</h3></div><div class='card-body'>" +
               "<form action='/generate-report' method='post'>" +
               "<div class='row'><div class='col-md-6 mb-3'>" +
               "<label class='form-label'>From Date</label>" +
               "<input name='fromDate' type='date' class='form-control'></div>" +
               "<div class='col-md-6 mb-3'><label class='form-label'>To Date</label>" +
               "<input name='toDate' type='date' class='form-control'></div></div>" +
               "<div class='row'><div class='col-md-6 mb-3'>" +
               "<label class='form-label'>Status</label>" +
               "<select name='status' class='form-control'><option value=''>All Status</option>" +
               "<option value='NEW'>New</option><option value='ACTIVE'>Active</option>" +
               "<option value='MATURED'>Matured</option><option value='RENEWED'>Renewed</option></select></div>" +
               "<div class='col-md-6 mb-3'><label class='form-label'>Person Name</label>" +
               "<input name='personName' class='form-control' placeholder='Enter person name'></div></div>" +
               "<div class='d-grid gap-2 d-md-flex justify-content-md-end'>" +
               "<button type='submit' class='btn btn-info me-2'><i class='fas fa-file-pdf'></i> Generate PDF Report</button>" +
               "<a href='/home' class='btn btn-secondary'>Back to Home</a></div>" +
               "</form></div></div></div></div></div></body></html>";
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