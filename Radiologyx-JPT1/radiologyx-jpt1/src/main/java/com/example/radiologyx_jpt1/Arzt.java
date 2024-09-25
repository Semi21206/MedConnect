package com.example.radiologyx_jpt1;

import jakarta.persistence.*;

@Entity
@Table(name = "a_arzt")
public class Arzt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "a_username", unique = true, nullable = false)
    private String username;

    @Column(name = "a_firstname", nullable = false)
    private String firstName;

    @Column(name = "a_lastname", nullable = false)
    private String lastName;

    @Column(name = "a_role", nullable = false)
    private String role = "ROLE_ARZT";  // Rolle als ROLE_ARZT festlegen

    @Column(name = "a_password", nullable = false)
    private String password;

    // Standard-Konstruktor
    public Arzt() {}

    public Arzt(Long id, String username, String firstName, String lastName, String role, String password) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
    }

    public Arzt(String username, String firstName, String lastName, String encode) {
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

