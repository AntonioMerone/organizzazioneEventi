package com.example.demo.payloads;

import com.example.demo.entities.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UserDTO(

        @NotBlank(message = "la mail è d'obbligo")
        @Email(message = "controlla come hai scritto, serve la @")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 5, message = "deve avere almeno 5 caratteri")
        String password,

        @NotNull(message = "Seleziona se sei Utente o Organizzatore")
        Ruolo ruolo


) {
}
