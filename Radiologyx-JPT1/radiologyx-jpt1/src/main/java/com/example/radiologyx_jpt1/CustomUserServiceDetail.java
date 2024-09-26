package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserServiceDetail implements UserDetailsService {
    private UserInterface userRepository;
    private ArztInterface arztInterface;


    @Autowired
    public CustomUserServiceDetail(UserInterface userRepository, ArztInterface arztInterface) {
        this.userRepository = userRepository;
        this.arztInterface = arztInterface;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            System.out.println("User gefunden: " + user.getUsername());
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),

                    Collections.singleton(new SimpleGrantedAuthority("ROLE_PATIENT"))
            );
        }

        // Arzt-Login überprüfen
        Optional<Arzt> optionalArzt = arztInterface.findByUsername(username);
        if (optionalArzt.isPresent()) {
            Arzt arzt = optionalArzt.get();
            return new org.springframework.security.core.userdetails.User(
                    arzt.getUsername(),
                    arzt.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ARZT")) // Rolle ARZT
            );
        }

        throw new UsernameNotFoundException("Username oder Passwort nicht gefunden!");

    }
}
