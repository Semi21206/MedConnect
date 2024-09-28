package com.example.radiologyx_jpt1;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "b_befunde")
public class Befund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)  // verweist auf User.id
    private User patient;

    // Beziehung zum Arzt (Arzt)
    @ManyToOne
    @JoinColumn(name = "arzt_id", referencedColumnName = "id", nullable = false)  // verweist auf Arzt.id
    private Arzt arzt;

    @Column(name = "b_dateiname")
    private String dateiname;

    @Column(name = "b_hochgeladenam")
    private LocalDateTime hochgeladenAm;

    public Befund() {
    }

    public Befund(Long id, User patient, Arzt arzt, String dateiname, LocalDateTime hochgeladenAm) {
        this.id = id;
        this.patient = patient;
        this.arzt = arzt;
        this.dateiname = dateiname;
        this.hochgeladenAm = hochgeladenAm;
    }

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

    public String getDateiname() {
        return dateiname;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public LocalDateTime getHochgeladenAm() {
        return hochgeladenAm;
    }

    public void setHochgeladenAm(LocalDateTime hochgeladenAm) {
        this.hochgeladenAm = hochgeladenAm;
    }

    public void setPatientId(Long patientId) {
        this.patient = new User(); // Erstellen eines neuen User-Objekts
        this.patient.setId(patientId); // Setzen der ID
    }

    public void setArztId(Long arztId) {
        this.arzt = new Arzt(); // Erstellen eines neuen Arzt-Objekts
        this.arzt.setId(arztId); // Setzen der ID
    }

}
