package com.example.demo.services;

import com.example.demo.entities.Evento;
import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.User;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.payloads.PrenotazioneDTO;
import com.example.demo.repositories.EventiRepository;
import com.example.demo.repositories.PrenotazioniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Service
@Slf4j
public class PrenotazioniService {
    private final PrenotazioniRepository prenotazioniRepository;
    private final EventiRepository eventiRepository;

    public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, EventiRepository eventiRepository) {
        this.prenotazioniRepository = prenotazioniRepository;
        this.eventiRepository = eventiRepository;
    }
    public Page<Prenotazione> findAll(int page, int size, String orderBy) {
        if (size > 200 || size <= 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return prenotazioniRepository.findAll(pageable);
}
    public Prenotazione savePrenotazione(PrenotazioneDTO payload, User currentUser) {

        Evento evento = eventiRepository.findById(payload.eventoid())
                .orElseThrow(() -> new NotFoundException("Evento con id " + payload.eventoid() + " non trovato"));

        // 1) no doppia prenotazione stesso utente + stesso evento
        if (prenotazioniRepository.existsByUser_IdAndEvento_Id(currentUser.getId(), evento.getId())) {
            throw new BadRequestException("Hai già una prenotazione per questo evento");
        }

        // 2) controllo posti
        long prenotazioniAttuali = prenotazioniRepository.countByEvento_Id(evento.getId());
        if (prenotazioniAttuali >= evento.getNumeroPostiEvento()) {
            throw new BadRequestException("Posti esauriti per questo evento");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUser(currentUser);
        prenotazione.setEvento(evento);
        prenotazione.setDataDellaPrenotazione(LocalDate.now());

        return prenotazioniRepository.save(prenotazione);
    }

    public void findByIdAndDelete(long prenotazioneId, User currentUser) {
        Prenotazione found = prenotazioniRepository.findById(prenotazioneId)
                .orElseThrow(() -> new NotFoundException("Prenotazione con id " + prenotazioneId + " non trovata"));

        // solo chi l'ha fatta può cancellarla
        if (found.getUser().getId() != currentUser.getId()) {
            throw new UnauthorizedException("Non puoi eliminare la prenotazione di un altro utente");
        }

        prenotazioniRepository.delete(found);
        log.info("Prenotazione eliminata. id={}", prenotazioneId);
    }
}
