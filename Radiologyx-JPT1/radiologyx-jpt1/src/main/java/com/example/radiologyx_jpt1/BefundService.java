package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BefundService {
    private final BefundInterface befundRepository;
    private final UserInterface userRepository;
    private final ArztInterface arztRepository;

    String uploadDirectory = Paths.get("src/main/resources/uploads").toAbsolutePath().toString();

    @Autowired
    public BefundService(BefundInterface befundRepository, UserInterface userRepository, ArztInterface arztRepository) {
        this.befundRepository = befundRepository;
        this.userRepository = userRepository;
        this.arztRepository = arztRepository;
    }

    public void uploadBefund(Long patientId, Long arztId, MultipartFile file, LocalDateTime hochgeladenAm) throws IOException {
        System.out.println("Upload Directory: " + uploadDirectory); // Protokolliere den Pfad
        try {
            User patient = userRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient nicht gefunden"));
            Arzt arzt = arztRepository.findById(arztId).orElseThrow(() -> new IllegalArgumentException("Arzt nicht gefunden"));

            // Überprüfe, ob die Datei leer ist
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Die hochgeladene Datei ist leer.");
            }

            // Generiere einen eindeutigen Dateinamen, falls der Originaldateiname bereits existiert
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                throw new IllegalArgumentException("Der Dateiname ist ungültig.");
            }

            // Generiere einen eindeutigen Dateinamen basierend auf dem Original
            String fileExtension = "";
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                fileExtension = originalFilename.substring(i);
            }
            String uniqueFilename = "befund_" + UUID.randomUUID().toString() + fileExtension;

            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Verwende REPLACE_EXISTING, um eine existierende Datei zu überschreiben
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Befund befund = new Befund();
            befund.setArzt(arzt); // Setze den Arzt
            befund.setPatient(patient); // Setze den Patienten
            befund.setDateiname(uniqueFilename); // Setze den eindeutigen Dateinamen
            befund.setHochgeladenAm(hochgeladenAm);
            befundRepository.save(befund);

            // Erfolgreiche Rückmeldung
            System.out.println("Befund erfolgreich hochgeladen: " + uniqueFilename);

        } catch (Exception e) {
            // Logge die Exception
            System.err.println("Fehler beim Hochladen des Befunds: " + e.getMessage());
            e.printStackTrace(); // Dies gibt den Stacktrace in der Konsole aus
            throw e; // Wirf die Exception weiter, falls notwendig
        }
    }

    //alle Befund abrufen
    public List<Befund> getBefundeforPatient(Long patientId) {
        User patient = userRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient nicht gefunden"));
        return befundRepository.findByPatient(patient);
    }

    public File getBefundFile(String dateiname) {
        return new File(uploadDirectory + "/" + dateiname);
    }
}