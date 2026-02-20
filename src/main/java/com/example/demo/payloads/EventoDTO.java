package com.example.demo.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventoDTO {

    @NotBlank(message = "inserire il nome dell'evento")
    private String nomeEvento;

    @NotBlank(message = "inserire descrizione evento")
    private String descrizioneEvento;

    @NotNull(message = "inserire la data dell'evento")
    @Future(message = "la data deve essere nel futuro")
    private LocalDate dataEvento;

    @NotBlank(message = "inserire il luogo dell'evento")
    private String luogoEvento;

    @Min(value = 1, message = "l'evento deve essere libero per partecipare di almeno 1 posto")
    private int numeroPostiEvento;
}