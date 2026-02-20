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
    List<Prenotazione> findByUser(Long userId);

    boolean existByUserAndEvento(User user, Evento evento);

    long DisponibilitaPostiEvento(Evento evento);

}
