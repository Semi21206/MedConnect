package com.example.radiologyx_jpt1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface AppointmentInterface extends JpaRepository<Appointment, Long> {
    List<Appointment> findByArzt(Arzt arzt);
    List<Appointment> findByPatient(User patient);
    List<Appointment> findByDateTime(LocalDateTime dateTime);
}
