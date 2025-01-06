package com.example.radiologyx_jpt1;

public interface UserService {

    User findByUsername(String username);

    User save(UserDTO userDto);

    User findById(Long id);

    // Neue Methode zum Speichern eines Arztes
    Arzt saveArzt(ArztDTO arztDto);
}
