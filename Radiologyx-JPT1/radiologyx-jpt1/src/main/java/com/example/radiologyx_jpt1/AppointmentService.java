package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentInterface appointmentInterface;

    public Appointment createAppointment(User patient, Arzt arzt, LocalDateTime dateTime) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setArzt(arzt);
        appointment.setDateTime(dateTime);
        return appointmentInterface.save(appointment);
    }
    public List<Appointment> getAllAppointments(Arzt arzt) {
        return appointmentInterface.findByArzt(arzt);
    }

}
