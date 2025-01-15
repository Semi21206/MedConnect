package com.example.radiologyx_jpt1;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class RadiologyxControllerTest {

    @InjectMocks
    private RadiologyxController radiologyxController;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private BefundService befundService;

    @Mock
    private BefundInterface befundInterface;

    @Mock
    private UserInterface userInterface;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    private User testPatient;
    private List<Befund> testBefunde;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testPatient = new User();
        testPatient.setId(1L);
        testPatient.setUsername("testPatient");

        testBefunde = new ArrayList<>();
        Befund befund = new Befund();
        befund.setId(1L);
        befund.setPatient(testPatient);
        befund.setDateiname("test.pdf");
        testBefunde.add(befund);
    }

    // Test für die Methode "befundeEinsehenForm"
    @Test
    void testBefundeEinsehenForm() {
        when(principal.getName()).thenReturn("testPatient");
        when(userInterface.findByUsername("testPatient")).thenReturn(testPatient);
        when(befundService.getBefundeforPatient(1L)).thenReturn(testBefunde);

        String viewName = radiologyxController.befundeEinsehenForm(model, principal);

        assertThat(viewName).isEqualTo("befunde-einsehen");
        verify(model).addAttribute(eq("befunde"), anyList());
    }

    // Test für die Methode "showAppointmentForm"
    @Test
    void testShowAppointmentForm() {
        when(principal.getName()).thenReturn("testPatient");
        when(userInterface.findByUsername("testPatient")).thenReturn(testPatient);

        String viewName = radiologyxController.showAppointmentForm(model, principal);

        assertThat(viewName).isEqualTo("termine-vereinbaren");
        verify(model).addAttribute(eq("availableDates"), anyList());
        verify(model).addAttribute(eq("appointment"), any(Appointment.class));
        verify(model).addAttribute(eq("patientId"), eq(1L));
        verify(model).addAttribute(eq("patient"), eq(testPatient));
    }
}
