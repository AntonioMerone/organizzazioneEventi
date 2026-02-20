package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        //tolto autenticazione via form (perchè lo fa react)
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        //2 non voglio protezione per gli CSRF perchè usiamo JWT
        httpSecurity.csrf(csrf -> csrf.disable());
        //3 Visto che usiamo JWT non posso lavorare con SESSIONI, quindi la faccio STATELESS
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //4 devo peronalizzare le funzionalità pre esistenti; così non mi torna di default errore 401
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());
        //5 termino
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getBCrypt(){
        return new BCryptPasswordEncoder(12);
    }
}
