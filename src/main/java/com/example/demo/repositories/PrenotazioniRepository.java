package com.example.demo.repositories;


import com.example.demo.entities.Evento;
import com.example.demo.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, Long> {

    //capisco e controllo le prentoazione se è fatta da utente o organizzatore
    List<Prenotazione> findByUser(Long userId);

    boolean existByUserAndEvento(long userId, long eventoId);

    long DisponibilitaPostiEvento(Evento evento);

}
