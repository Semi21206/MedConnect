package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserServiceDetail customUserDetailService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Optional: Cookie CSRF-Token verwenden
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/impressum", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        //ARZT
                        .requestMatchers("/arzt/befund-hochladen").hasRole("ARZT") // Nur für Ärzte
                        .requestMatchers("/arzt/termine-einsehen").hasRole("ARZT") // Nur für Ärzte
                        //PATIENT
                        .requestMatchers("/patient/befunde-einsehen").hasRole("PATIENT")   // Nur Patienten dürfen auf "/patient/befunde-einsehen" zugreifen
                        .requestMatchers("/patient/termine-vereinbaren").hasRole("PATIENT")   // Nur Patienten dürfen auf "/patient/termine-vereinbaren.html" zugreifen
                        .anyRequest().authenticated() // Alle anderen Seiten erfordern Authentifizierung
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true) // Hier zur Hauptseite weiterleiten
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }
}
