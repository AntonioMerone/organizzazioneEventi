package com.example.demo.payloads;

import jakarta.validation.constraints.NotNull;

public record PrenotazioneDTO(

        @NotNull(message = "specificare l'evento è obbligatorio")
        Long eventoid

        //no Utente id

) {
}
