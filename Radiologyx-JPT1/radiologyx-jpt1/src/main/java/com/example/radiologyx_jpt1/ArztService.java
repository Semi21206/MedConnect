package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArztService {
    @Autowired
    private ArztInterface arztInterface;

    public Arzt findById(Long id) {
        Optional<Arzt> optionalArzt = arztInterface.findById(id);
        return optionalArzt.orElse(null);
    }
}
