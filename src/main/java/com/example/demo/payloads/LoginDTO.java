package com.example.demo.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "la mail è obbligaoria")
        @Email(message = "controlla se la mail isnerita è corretta")
        String email,
        String password) {
}

