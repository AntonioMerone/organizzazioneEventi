package com.example.demo.controllers;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.User;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.exceptions.ValidationException;
import com.example.demo.payloads.PrenotazioneDTO;
import com.example.demo.services.PrenotazioniService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @GetMapping
    public Page<Prenotazione> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataDellaPrenotazione") String orderBy
    ) {
        return this.prenotazioniService.findAll(page, size, orderBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione create(@RequestBody @Valid PrenotazioneDTO payload,
                               BindingResult validationResult,
                               @AuthenticationPrincipal User currentUser) throws BadRequestException {

        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors().stream()
                            .map(fieldError -> fieldError.getDefaultMessage())
                            .toList()
            );
        }

        if (currentUser == null) throw new UnauthorizedException("Devi essere autenticato");

        return prenotazioniService.savePrenotazione(payload, currentUser);
    }

    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("prenotazioneId") long prenotazioneId,
                       @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) throw new UnauthorizedException("Devi essere autenticato");

        prenotazioniService.findByIdAndDelete(prenotazioneId, currentUser);
    }
}

