package com.example.demo.controller;
import com.example.demo.service.PDFGenerationService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PDFGenerationController {

    @Autowired
    private PDFGenerationService pdfGenerationService;

    @GetMapping("/bank-letter")
    public ResponseEntity<byte[]> generateBankLetterPDF() {
        try {
            // Simulated data for bank, company, and facility details
            Map<String, String> bankDetails = new HashMap<>();
            bankDetails.put("bankName", "Sample Bank");

            Map<String, String> companyDetails = new HashMap<>();
            companyDetails.put("companyName", "ABC Company");
            companyDetails.put("companyAddress", "123 Street, City");
            companyDetails.put("contactDetails", "Email: abc@example.com");

            Map<String, String> facilityDetails = new HashMap<>();
            facilityDetails.put("date", "2024-03-22");
            facilityDetails.put("facility", "Loan Facility");
            facilityDetails.put("existingLimit", "$100,000");
            facilityDetails.put("sanctionedLimit", "$150,000");

            ByteArrayOutputStream pdfOutputStream = pdfGenerationService.generateBankLetterPDF(bankDetails, companyDetails, facilityDetails);

            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "bank_letter.pdf");

            return new ResponseEntity<>(pdfOutputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
