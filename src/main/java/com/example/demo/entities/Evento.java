package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Eventi")
@Getter
@Setter
@ToString
@NoArgsConstructor


public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String nomeEvento;
    private String descrizioneEvento;
    private LocalDate dataEvento;
    private String luogoEvento;
    private int numeroPostiEvento;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    public Evento(String nomeEvento, String descrizioneEvento, LocalDate dataEvento, String luogoEvento, int numeroPostiEvento, User user) {
        this.nomeEvento = nomeEvento;
        this.descrizioneEvento = descrizioneEvento;
        this.dataEvento = dataEvento;
        this.luogoEvento = luogoEvento;
        this.numeroPostiEvento = numeroPostiEvento;
        this.user = user;
    }
}
