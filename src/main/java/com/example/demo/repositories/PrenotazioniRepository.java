package com.example.demo.repositories;


import com.example.demo.entities.Evento;
import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, Long> {

    //capisco e controllo le prentoazione se è fatta da utente o organizzatore
    // prenotazioni di un utente
    List<Prenotazione> findByUser_Id(Long userId);

    // evita doppia prenotazione: stesso utente sullo stesso evento
    boolean existsByUser_IdAndEvento_Id(Long userId, Long eventoId);

    // conta prenotazioni per capire se ci sono posti
    long countByEvento_Id(Long eventoId);

}

