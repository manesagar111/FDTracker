package com.example.fdtracker.service;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.repository.FixedDepositRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final FixedDepositRepository repository;
    public EmailService(JavaMailSender mailSender, FixedDepositRepository repository) {
        this.mailSender = mailSender;
        this.repository = repository;
    }
    
    public void sendMaturityNotification(List<FixedDeposit> maturedDeposits) {
        try {
            String htmlContent = buildHtmlTable(maturedDeposits);
            
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            
            helper.setFrom("fdtracker@example.com");
            helper.setTo("manesagar100@gmail.com");
            helper.setSubject("Fixed Deposit Maturity Alert");
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
            System.out.println("Email sent successfully to manesagar100@gmail.com");
            
            // Update mail sent flag and notification date for each FD
            for (FixedDeposit fd : maturedDeposits) {
                fd.setMailSent(true);
                fd.setLastNotificationDate(LocalDate.now());
                repository.save(fd);
            }
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    private String buildHtmlTable(List<FixedDeposit> deposits) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body><h3>Fixed Deposit Maturity Alert</h3>");
        
        if (deposits.isEmpty()) {
            html.append("<p>There is no FD to renew for the specified period.</p>");
        } else {
            html.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            html.append("<tr style='background-color: #f2f2f2;'>");
            html.append("<th style='padding: 8px;'>Person Name</th><th style='padding: 8px;'>Bank Name</th>");
            html.append("<th style='padding: 8px;'>Account</th><th style='padding: 8px;'>From Date</th>");
            html.append("<th style='padding: 8px;'>To Date</th><th style='padding: 8px;'>Invested Amount</th>");
            html.append("<th style='padding: 8px;'>Return Amount</th><th style='padding: 8px;'>Status</th>");
            html.append("<th style='padding: 8px;'>Description</th></tr>");
            
            for (FixedDeposit fd : deposits) {
                html.append("<tr>");
                html.append("<td style='padding: 8px;'>").append(fd.getPersonName()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getBankName()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getMaskedAccountNumber()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getFromDate()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getToDate()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getInvestedAmount()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getReturnAmount()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getStatus()).append("</td>");
                html.append("<td style='padding: 8px;'>").append(fd.getDescription()).append("</td>");
                html.append("</tr>");
            }
            html.append("</table>");
        }
        
        html.append("<br><br><p>Thanks & Regards,<br>Sagar Mane<br>Sr. Java Developer</p>");
        html.append("</body></html>");
        return html.toString();
    }
}