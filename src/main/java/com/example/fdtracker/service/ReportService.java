package com.example.fdtracker.service;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.model.Status;
import com.example.fdtracker.repository.FixedDepositRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    
    private final FixedDepositRepository repository;
    
    public ReportService(FixedDepositRepository repository) {
        this.repository = repository;
    }
    
    public byte[] generatePdfReport(LocalDate fromDate, LocalDate toDate, String status, String personName) {
        try {
            // Handle empty strings as null for optional parameters
            Status statusParam = null;
            if (status != null && !status.trim().isEmpty()) {
                try {
                    statusParam = Status.valueOf(status.trim());
                } catch (IllegalArgumentException e) {
                    statusParam = null;
                }
            }
            String personNameParam = (personName != null && !personName.trim().isEmpty()) ? personName : null;
            
            List<FixedDeposit> deposits;
            
            // Filter based on parameter combinations
            if (fromDate == null && toDate == null && statusParam == null && personNameParam == null) {
                deposits = repository.findAll();
            } else if (fromDate != null && toDate != null && statusParam != null && personNameParam != null) {
                deposits = repository.findByAllParams(fromDate, toDate, statusParam, personNameParam);
            } else if (fromDate != null && toDate != null && statusParam != null) {
                deposits = repository.findByDateRangeAndStatus(fromDate, toDate, statusParam);
            } else if (fromDate != null && toDate != null && personNameParam != null) {
                deposits = repository.findByDateRangeAndPersonName(fromDate, toDate, personNameParam);
            } else if (fromDate != null && toDate != null) {
                deposits = repository.findByDateRange(fromDate, toDate);
            } else if (statusParam != null && personNameParam != null) {
                deposits = repository.findByStatusAndPersonName(statusParam, personNameParam);
            } else if (toDate != null) {
                deposits = repository.findByToDateOnly(toDate);
            } else if (statusParam != null) {
                deposits = repository.findByStatus(statusParam);
            } else if (personNameParam != null) {
                deposits = repository.findByPersonName(personNameParam);
            } else {
                deposits = repository.findAll();
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            document.add(new Paragraph("Fixed Deposit Report").setBold().setFontSize(16));
            document.add(new Paragraph("Generated on: " + LocalDate.now()));
            
            Table table = new Table(9);
            table.addHeaderCell(new Cell().add(new Paragraph("Person Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Bank Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Account").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("From Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("To Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Invested Amount").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Return Amount").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));
            
            for (FixedDeposit fd : deposits) {
                table.addCell(fd.getPersonName());
                table.addCell(fd.getBankName());
                table.addCell(fd.getMaskedAccountNumber());
                table.addCell(fd.getFromDate().toString());
                table.addCell(fd.getToDate().toString());
                table.addCell(fd.getInvestedAmount().toString());
                table.addCell(fd.getReturnAmount().toString());
                table.addCell(fd.getStatus() != null ? fd.getStatus().toString() : "");
                table.addCell(fd.getDescription() != null ? fd.getDescription() : "");
            }
            
            document.add(table);
            document.close();
            
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }
}