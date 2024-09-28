package com.example.radiologyx_jpt1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.Remote;
import java.util.List;

public interface BefundInterface extends JpaRepository<Befund, Long> {
    List<Befund> findByPatient(User patient);
}
