package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        .ignoringRequestMatchers("/api/**") // CSRF für API-Endpunkte deaktivieren
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Optional: Cookie CSRF-Token verwenden
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/impressum", "/css/**", "/js/**", "/images/**", "/termine-vereinbaren-success").permitAll()
                        .requestMatchers("/register", "/login").not().authenticated() // Nicht zugänglich für angemeldete Nutzer
                        //ARZT
                        .requestMatchers("/arzt/befunde-patientenliste").hasRole("ARZT")
                        .requestMatchers(HttpMethod.GET, "/arzt/befund-hochladen").hasRole("ARZT")
                        .requestMatchers(HttpMethod.POST, "/arzt/befund-hochladen").hasRole("ARZT")
                        .requestMatchers("/arzt/termine-einsehen").hasRole("ARZT") // Nur für Ärzte

                        //PATIENT
                        .requestMatchers("/patient/befunde-einsehen").hasRole("PATIENT")   // Nur Patienten dürfen auf "/patient/befunde-einsehen" zugreifen
                        .requestMatchers("/patient/termine-vereinbaren").hasRole("PATIENT")   // Nur Patienten dürfen auf "/patient/termine-vereinbaren.html" zugreifen
                        .anyRequest().authenticated() // Alle anderen Seiten erfordern Authentifizierung
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true") // Bei Fehler zu dieser URL weiterleiten
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
