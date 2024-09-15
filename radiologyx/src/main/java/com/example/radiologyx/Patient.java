package com.example.radiologyx;

import jakarta.persistence.*;

@Entity
@Table(name = "p_patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


}
