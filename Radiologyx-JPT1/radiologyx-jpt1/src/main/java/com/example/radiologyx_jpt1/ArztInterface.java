package com.example.radiologyx_jpt1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ArztInterface extends JpaRepository<Arzt,Long> {
    Optional<Arzt> findByUsername(String username);
}
