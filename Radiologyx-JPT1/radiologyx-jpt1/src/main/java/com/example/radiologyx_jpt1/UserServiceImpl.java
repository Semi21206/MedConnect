package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserInterface userRepository;
    @Autowired
    private ArztInterface arztInterface;

    public UserServiceImpl(UserInterface userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserDTO userDto) {
        // Passwortvalidierung hinzufügen
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwörter stimmen nicht überein");
        }
        String role = "ROLE_PATIENT";
        User user = new User(userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
        userDto.getGender(), passwordEncoder.encode(userDto.getPassword()), role);
        return userRepository.save(user);
    }

    // Neue Methode zum Speichern eines Arztes
    @Override
    public Arzt saveArzt(ArztDTO arztDto) {
        // Passwortvalidierung hinzufügen
        if (!arztDto.getPassword().equals(arztDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwörter stimmen nicht überein");
        }

        Arzt arzt = new Arzt(
                arztDto.getUsername(),
                arztDto.getFirstName(),
                arztDto.getLastName(),
                passwordEncoder.encode(arztDto.getPassword()) // Verschlüsseltes Passwort verwenden
        );

        arzt.setRole(arztDto.getRole()); // Rolle setzen

        return arztInterface.save(arzt); // Arzt in der Datenbank speichern
    }
}
