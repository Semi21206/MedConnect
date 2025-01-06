package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentInterface appointmentInterface;

    public void createAppointment(User patient, Arzt arzt, LocalDateTime dateTime) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setArzt(arzt);
        appointment.setDateTime(dateTime);
        appointmentInterface.save(appointment);
    }

    public boolean isSlotAvailable(LocalDateTime dateTime) {
        return appointmentInterface.findByDateTime(dateTime).isEmpty();
    }

    public List<Appointment> getAllAppointments(Arzt arzt) {
        return appointmentInterface.findByArzt(arzt);
    }


}
