package com.example.radiologyx_jpt1;

import jakarta.persistence.*;

@Entity
@Table(name = "u_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "u_username", unique = true, nullable = false)
    private String username;
    @Column(name = "u_firstname")
    private String firstName;
    @Column(name = "u_lastname")
    private String lastName;
    @Column(name = "u_gender")
    private String gender;
    @Column(name = "u_svnr")
    private Integer svnr;

    public Integer getSvnr() {
        return svnr;
    }

    public void setSvnr(Integer svnr) {
        this.svnr = svnr;
    }

    public User(Long id, String username, String firstName, String lastName, String gender, Integer svnr, String password, String role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.svnr = svnr;
        this.password = password;
        this.role = role;
    }

    @Column(name = "u_password")
    private String password;
    @Column(name = "u_role", nullable = false)  // Neue Spalte für die Rolle
    private String role = "ROLE_PATIENT";  // Rolle als ROLE_PATIENT festlegen
    public User() {
    }

    public User(String username, String firstName, String lastName, String gender, int svnr, String password, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.svnr = svnr;
        this.password = password;
        this.role = role;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
