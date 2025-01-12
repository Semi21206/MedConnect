package com.example.radiologyx_jpt1;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DoctorInitializer implements ApplicationRunner {
    private final ArztInterface arztInterface;

    public DoctorInitializer(ArztInterface arztInterface) {
        this.arztInterface = arztInterface;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("PostConstruct wird ausgeführt...");
        if (arztInterface.findByUsername("drmustermann").isEmpty()) {
            Arzt doctor = new Arzt();
            doctor.setId(1L);
            doctor.setFirstName("Max");
            doctor.setLastName("Mustermann");
            doctor.setUsername("drmustermann");
            doctor.setPassword(new BCryptPasswordEncoder().encode("max1234"));
            doctor.setRole("ARZT");

            arztInterface.save(doctor);
            System.out.println("Arzt wurde erfolgreich erstellt.");
        }
    }
}
