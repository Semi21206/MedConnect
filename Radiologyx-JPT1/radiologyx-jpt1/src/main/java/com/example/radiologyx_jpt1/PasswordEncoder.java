package com.example.radiologyx_jpt1;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Diese Klasse dient nur zur Entcodung des Passwords, um das verschlüsselte Passwort des Arztes
// in die Datenbank einzufügen
public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "susi1234"; // Ersetzen Sie dies durch das tatsächliche Passwort
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword); // Ausgabe des codierten Passworts
    }
}
