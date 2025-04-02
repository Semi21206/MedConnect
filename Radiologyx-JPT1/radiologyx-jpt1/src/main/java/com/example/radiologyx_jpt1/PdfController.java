package com.example.radiologyx_jpt1;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
public class PdfController {

    @Autowired
    private BefundService befundService;

    @PostMapping("/api/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody Map<String, Object> body) {
        try {
            String htmlContent = (String) body.get("htmlContent");
            Long patientId = Long.parseLong(body.get("patientId").toString());
            Long arztId = Long.parseLong(body.get("arztId").toString());

            // Generate a unique filename
            String fileName = "befund_" + UUID.randomUUID().toString() + ".pdf";
            String filePath = System.getProperty("user.dir") + "/src/main/resources/uploads/" + fileName;

            // Create PDF from HTML content
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            ConverterProperties converterProperties = new ConverterProperties();

            // Convert HTML to PDF
            HtmlConverter.convertToPdf(htmlContent, pdfOutputStream, converterProperties);
            byte[] pdfBytes = pdfOutputStream.toByteArray();

            // Save PDF file to uploads directory
            Path path = Paths.get(filePath);
            Files.write(path, pdfBytes);

            // Create a MultipartFile from the PDF bytes
            MultipartFile multipartFile = new MockMultipartFile(
                    fileName,
                    fileName,
                    "application/pdf",
                    pdfBytes
            );

            // Save the Befund in the database through BefundService
            LocalDateTime now = LocalDateTime.now();
            befundService.uploadBefund(patientId, arztId, multipartFile, now);

            // Return the PDF for preview
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline().filename(fileName).build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper class to create a MultipartFile from a byte array
    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public MockMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}