package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RadiologyxController {

    @Autowired
    private UserService userService; // UserService wird genutzt, um die Registrierung zu handhaben

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
        model.addAttribute("user", new UserDTO()); // Leeres UserDTO für das Formular
        return "register"; // Das Template "register.html" wird geladen
    }

    // Registrierung - Formular abschicken
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
        try {
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
}
