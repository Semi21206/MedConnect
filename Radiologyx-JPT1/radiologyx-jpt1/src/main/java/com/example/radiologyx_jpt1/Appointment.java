package com.example.radiologyx_jpt1;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_termine")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false) // Der Join-Spaltenname für die Patienten-ID
    private User patient;

    @ManyToOne
    @JoinColumn(name = "arzt_id", nullable = false) // Der Join-Spaltenname für die Arzt-ID
    private Arzt arzt;

    private LocalDateTime dateTime;

    // Standard-Konstruktor
    public Appointment() {
    }

    public Appointment(Long id, User patient, Arzt arzt, LocalDateTime dateTime) {
        this.id = id;
        this.patient = patient;
        this.arzt = arzt;
        this.dateTime = dateTime;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Arzt getArzt() {
        return arzt;
    }

    public void setArzt(Arzt arzt) {
        this.arzt = arzt;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
