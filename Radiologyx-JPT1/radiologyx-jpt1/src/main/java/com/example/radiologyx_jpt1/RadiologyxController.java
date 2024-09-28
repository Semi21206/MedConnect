package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
public class RadiologyxController {

    @Autowired
    private UserService userService; // UserService wird genutzt, um die Registrierung zu handhaben
    @Autowired
    private BefundService befundService;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private BefundInterface befundInterface;

    // Home-Seite
    @GetMapping("/")
    public String demo() {
        return "index";
    }

    // Login-Seite
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User()); // User ist deine Klasse für das Login
        return "login"; // Name des Templates ohne .html
    }

    // Registrierung - Formular anzeigen
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDTO()); // Ein leeres User-Objekt hinzufügen
        System.out.println("Register method called"); // Protokollausgabe
        return "register"; // Das Template "register.html" wird geladen
    }

    // Registrierung - Formular abschicken
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
        try {
            userDTO.setRole("PATIENT");
            // Benutzer registrieren
            userService.save(userDTO); // Existierende Methode "save" verwenden

            // Erfolgsmeldung
            model.addAttribute("successMessage", "Registrierung war erfolgreich");
            return "login"; // Nach erfolgreicher Registrierung zur Login-Seite weiterleiten
        } catch (Exception e) {
            // Fehlermeldung anzeigen, wenn etwas schief geht
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // Bei Fehler zurück zum Registrierungsformular
        }
    }

    // Impressum-Seite
    @Controller
    public class MainController {
        @GetMapping("/impressum")
        public String impressum() {
            return "impressum"; // Das Template "impressum.html" wird geladen
        }
    }

    //ARZT ONLY
    @GetMapping("/arzt/befund-hochladen")
    public String befundHochladenForm(Model model) {
        return "befund-hochladen";
    }

    @GetMapping("/arzt/befunde-patientenliste")
    public String patientenListe(Model model) {
        List<User> users = userInterface.showUsers();
        model.addAttribute("users", users);
        return "befunde-patientenliste";
    }

    @PostMapping("/arzt/befund-hochladen")
    public String uploadBefund(@RequestParam("patientId") Long patientId,
                               @RequestParam("arztId") Long arztId,
                               @RequestParam("file") MultipartFile file,
                               Model model) {
        try {
            befundService.uploadBefund(patientId, arztId, file);
            model.addAttribute("successMessage", "Befund wurde erfolgreich hochgeladen.");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Fehler beim Hochladen des Befunds.");
        }
        return "befund-hochladen";  // Zurück zur Upload-Seite
    }

    @GetMapping("/arzt/termine-einsehen")
    public String termineEinsehenForm(Model model) {
        return "termine-einsehen";
    }

    //PATIENT ONLY
    @GetMapping("/patient/termine-vereinbaren")
    public String termineVereinbarenForm(Model model) {
        return "termine-vereinbaren.html";
    }

    @GetMapping("/patient/befunde-einsehen")
    public String befundeEinsehenForm(Model model, Principal principal) {
        User patient = userInterface.findByUsername(principal.getName());
        List<Befund> befunde = befundInterface.findByPatient(patient);
        model.addAttribute("befunde", befunde);
        return "befunde-einsehen";
    }

    @GetMapping("/download/{dateiname}")
    public ResponseEntity<FileSystemResource> downloadBefund(@PathVariable String dateiname) {
        try {
            Path filePath = Paths.get("src/main/resources/uploads/" + dateiname);
            FileSystemResource fileResource = new FileSystemResource(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + dateiname);

            return new ResponseEntity<>(fileResource, headers, HttpStatus.OK);
        } catch (Exception e) {
            // Fehlerbehandlung, falls die Datei nicht gefunden wird
            return ResponseEntity.notFound().build();
        }
    }


}
