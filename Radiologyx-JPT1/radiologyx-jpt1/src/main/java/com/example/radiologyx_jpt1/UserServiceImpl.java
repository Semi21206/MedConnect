package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserInterface userRepository;

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

        User user = new User(userDto.getUsername(), userDto.getFirstName(), userDto.getLastName(),
        userDto.getGender(), passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }
}
