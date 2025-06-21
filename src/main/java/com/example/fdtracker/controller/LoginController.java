package com.example.fdtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    
    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "<!DOCTYPE html><html><head><title>FD Tracker</title>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "</head><body class='bg-light'>" +
               "<div class='container mt-5'><div class='row justify-content-center'>" +
               "<div class='col-md-8'><div class='card shadow'>" +
               "<div class='card-header bg-primary text-white text-center'><h2>FD Tracker Dashboard</h2></div>" +
               "<div class='card-body text-center'>" +
               "<div class='row'><div class='col-md-4 mb-3'>" +
               "<a href='/records' class='btn btn-success btn-lg w-100'><i class='fas fa-list'></i> View Records</a></div>" +
               "<div class='col-md-4 mb-3'><a href='/add' class='btn btn-primary btn-lg w-100'><i class='fas fa-plus'></i> Add FD</a></div>" +
               "<div class='col-md-4 mb-3'><a href='/report' class='btn btn-info btn-lg w-100'><i class='fas fa-file-pdf'></i> Generate Report</a></div>" +
               "</div></div></div></div></div></div></body></html>";
    }
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "<h1>FD Tracker is running successfully!</h1><p><a href='/home'>Go to Home</a></p>";
    }
    
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        
        return "login";
    }
}