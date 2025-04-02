package com.example.radiologyx_jpt1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInterface extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Query("SELECT u from User u")
    List<User> showUsers();  //  Gibt alle Benutzer zurück
}
