package com.example.demo.services;

import com.example.demo.entities.Evento;
import com.example.demo.entities.User;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.payloads.EventoDTO;
import com.example.demo.repositories.EventiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
    public class EventiService {

        private final EventiRepository eventiRepository;

        public EventiService(EventiRepository eventiRepository) {
            this.eventiRepository = eventiRepository;
        }

        public Evento saveEvento(EventoDTO payload, User organizzatore) {

            if (organizzatore == null) throw new UnauthorizedException("Devi essere autenticato");
            if (!organizzatore.getRuolo().name().equals("ORGANIZZATORE")) {
                throw new UnauthorizedException("Solo un ORGANIZZATORE può creare eventi");
            }

            Evento evento = new Evento();
            evento.setNomeEvento(payload.getNomeEvento());
            evento.setDescrizioneEvento(payload.getDescrizioneEvento());
            evento.setDataEvento(payload.getDataEvento());
            evento.setLuogoEvento(payload.getLuogoEvento());
            evento.setNumeroPostiEvento(payload.getNumeroPostiEvento());
            evento.setUser(organizzatore);

            Evento saved = eventiRepository.save(evento);
            log.info("Evento salvato correttamente. id={}", saved.getId());
            return saved;
        }

        public Evento findById(long id) {
            return eventiRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Evento con id " + id + " non trovato"));
        }

        public Page<Evento> findAll(int page, int size, String orderBy) {
            if (size > 200 || size <= 0) size = 10;
            if (page < 0) page = 0;

            Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
            return eventiRepository.findAll(pageable);
        }

        public Evento findByIdAndUpdate(long id, EventoDTO body, User organizzatore) {
            if (organizzatore == null) throw new UnauthorizedException("Devi essere autenticato");
            if (!organizzatore.getRuolo().name().equals("ORGANIZZATORE")) {
                throw new UnauthorizedException("Solo un ORGANIZZATORE può modificare eventi");
            }

            Evento found = this.findById(id);

            // solo chi l'ha creato
            if (found.getUser().getId() != organizzatore.getId()) {
                throw new UnauthorizedException("Puoi modificare solo i tuoi eventi");
            }

            found.setNomeEvento(body.getNomeEvento());
            found.setDescrizioneEvento(body.getDescrizioneEvento());
            found.setDataEvento(body.getDataEvento());
            found.setLuogoEvento(body.getLuogoEvento());
            found.setNumeroPostiEvento(body.getNumeroPostiEvento());

            Evento updated = eventiRepository.save(found);
            log.info("Evento aggiornato correttamente. id={}", updated.getId());
            return updated;
        }

        public void findByIdAndDelete(long id, User organizzatore) {
            if (organizzatore == null) throw new UnauthorizedException("Devi essere autenticato");
            if (!organizzatore.getRuolo().name().equals("ORGANIZZATORE")) {
                throw new UnauthorizedException("Solo un ORGANIZZATORE può eliminare eventi");
            }

            Evento found = this.findById(id);


            if (found.getUser().getId() != organizzatore.getId()) {
                throw new UnauthorizedException("Puoi eliminare solo i tuoi eventi");
            }

            eventiRepository.delete(found);
            log.info("Evento eliminato correttamente. id={}", id);
        }
}