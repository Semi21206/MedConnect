package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private final UserInterface userInterface;

    public PatientService(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public User findById(Long id) {
        Optional<User> optionalPatient = userInterface.findById(id);
        return optionalPatient.orElse(null); // oder du kannst eine benutzerdefinierte Ausnahme werfen
    }
}
