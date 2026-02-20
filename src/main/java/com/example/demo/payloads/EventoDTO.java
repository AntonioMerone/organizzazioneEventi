package com.example.demo.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class EventoDTO {

    @NotBlank(message = "inserire il nome dell'evento")
    private String nomeEvento;

    @NotBlank(message = "inserire descrizione evento")
    private String descrizioneEvento;

    @NotNull(message = "inserire la data dell'evento")
    @Future(message = "la data deve essere nel futuro")
    private LocalDateTime dataEvento;

    @NotBlank(message = "inserire il luogo dell'evento")
    private String luogoEvento;

    @Min(value = 1, message = "l'evento deve essere libero per partecipare di almeno 1 posto")
    private int numeroPostiEvento;


}
