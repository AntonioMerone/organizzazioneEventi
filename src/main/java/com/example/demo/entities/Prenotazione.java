package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@ToString
@NoArgsConstructor


public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter (AccessLevel.NONE)
    private long id;

    private LocalDate dataDellaPrenotazione;
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "eventi_id", nullable = false)
    private Evento evento;

    public Prenotazione(LocalDate dataDellaPrenotazione, User user, Evento evento) {
        this.dataDellaPrenotazione = dataDellaPrenotazione;
        this.user = user;
        this.evento = evento;
    }
}
