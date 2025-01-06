package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ArztInterface arztInterface;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ArztService arztService;
    @Autowired
    private UserServiceImpl userServiceImpl;

    // Home-Seite
    @GetMapping("/")
    public String demo(@RequestParam(required = false) Long patientId, Model model) {
        // Wenn patientId vorhanden ist, hole den Patienten aus der Datenbank
        if (patientId != null) {
            User patient = userInterface.findById(patientId).orElse(null);
            model.addAttribute("patientId", patientId);  // patientId ins Modell hinzufügen
        }
        return "index";  // Deine Startseite
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
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, Model model, RedirectAttributes redirectAttributes) {
            userDTO.setRole("PATIENT");
            if (!userDTO.getFirstName().matches("[A-ZÄÖÜ][a-zäöüß]*") || !userDTO.getLastName().matches("[A-ZÄÖÜ][a-zäöüß]*")) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Bitte geben Sie einen gültigen Vor- und Nachnamen ein (Großbuchstaben am Anfang).");
                return "redirect:/register";
            }
            else {
                userService.save(userDTO); // Existierende Methode "save" verwenden
                // Benutzer registrieren
                // Erfolgsmeldung
                model.addAttribute("successMessage", "Registrierung war erfolgreich");
                return "login"; // Nach erfolgreicher Registrierung zur Login-Seite weiterleiten
        }
    }

    // Impressum-Seite
    @Controller
    public class MainController {
        @GetMapping("/impressum")
        public String impressum() {
            return "impressum";
        }
    }

    //ARZT ONLY
    @GetMapping("/arzt/befund-hochladen/{userId}")
    public String befundHochladenForm(@PathVariable Long userId, Model model) {
        User patient = userInterface.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);  // Den Patienten in die View übergeben
        return "befund-hochladen";
    }

    @GetMapping("/arzt/befunde-patientenliste")
    public String patientenListe(Model model) {
        List<User> users = userInterface.showUsers();
        model.addAttribute("users", users);
        return "befunde-patientenliste";
    }

    @PostMapping("/arzt/befund-hochladen/{patientId}")
    public String uploadBefund(@PathVariable("patientId") Long patientId,
                               @RequestParam("arztId") Long arztId,
                               @RequestParam("file") MultipartFile file,
                               Model model) {
        try {
            befundService.uploadBefund(patientId, arztId, file);
            model.addAttribute("successMessage", "Befund wurde erfolgreich hochgeladen.");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Fehler beim Hochladen des Befunds.");
        }
        User patient = userInterface.findById(patientId).orElse(null);
        model.addAttribute("patient", patient);

        return "befund-hochladen";  // Zurück zur Upload-Seite
    }

    @GetMapping("/arzt/termine-einsehen")
    public String termineEinsehenForm(Model model,Long arztId) {
        arztId = 1L;
        Arzt arzt = arztService.findById(arztId);
        List<Appointment> appointments = appointmentService.getAllAppointments(arzt);
        model.addAttribute("appointments", appointments);
        return "termine-einsehen"; // Thymeleaf-Template für die Anzeige der Termine
    }

    //PATIENT ONLY

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

    @GetMapping("/patient/termine-vereinbaren/{patientId}")
    public String showAppointmentForm(@PathVariable Long patientId, Model model) {
        // Patient aus der Datenbank laden
        User patient = userInterface.findById(patientId).orElse(null);
//        if (patient == null) {
//            model.addAttribute("errorMessage", "Patient nicht gefunden.");
//            return "error";  // Optional: Fehlerseite
//        }
        model.addAttribute("availableDates", getAvailableDates());
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patientId", patientId);  // PatientId hinzufügen
        model.addAttribute("patient", patient);  // Patient zum Modell hinzufügen

        return "termine-vereinbaren";  // Dein Template für Termine
    }

    // Verfügbare Zeiten für ein ausgewähltes Datum abrufen
    @GetMapping("/patient/get-timeslots")
    @ResponseBody
    public List<LocalTime> getAvailableTimeslots(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return new ArrayList<>();
        }
        return getTimeslots().stream()
                .filter(time -> appointmentService.isSlotAvailable(date.atTime(time)))
                .collect(Collectors.toList());
    }

    // Termin erstellen
    @PostMapping("/patient/termine-vereinbaren")
    public String createAppointment(@RequestParam("patientId") Long patientId,
                                    @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                    Model model) {
        User patient = userService.findById(patientId);
        if (patient == null) {
            model.addAttribute("errorMessage", "Patient nicht gefunden.");
            return "error";
        }

        Arzt arzt = arztService.findById(1L);  // Beispiel: Es gibt nur einen Arzt mit ID 1
        appointmentService.createAppointment(patient, arzt, dateTime);
        model.addAttribute("successMessage", "Termin erfolgreich erstellt.");
        return "redirect:/patient/termine-vereinbaren";
    }


    // Verfügbare Tage berechnen (nur werktags)
    private List<LocalDate> getAvailableDates() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);
        List<LocalDate> dates = new ArrayList<>();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                dates.add(date);
            }
        }
        return dates;
    }

    // Verfügbare Zeitslots berechnen (werktags von 8:00 bis 16:30 Uhr, alle 30 Minuten)
    private List<LocalTime> getTimeslots() {
        List<LocalTime> timeslots = new ArrayList<>();
        LocalTime time = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(16, 30);
        while (time.isBefore(closingTime)) {
            timeslots.add(time);
            time = time.plusMinutes(30);
        }
        return timeslots;
    }
}

