package com.example.demo.controllers;
import com.example.demo.entities.Evento;
import com.example.demo.entities.User;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.exceptions.ValidationException;
import com.example.demo.payloads.EventoDTO;
import com.example.demo.services.EventiService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventi")
public class EventiController {
    private final EventiService eventiService;

    public EventiController(EventiService eventiService) {
        this.eventiService = eventiService;
    }

    @GetMapping
    public Page<Evento> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataEvento") String orderBy
    ) {
        return this.eventiService.findAll(page, size, orderBy);
    }

    @GetMapping("/{eventoId}")
    public Evento findById(@PathVariable("eventoId") long eventoId) {
        return this.eventiService.findById(eventoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evento create(@RequestBody @Valid EventoDTO payload,
                         BindingResult validationResult,
                         @AuthenticationPrincipal User currentUser) {

        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors().stream()
                            .map(fieldError -> fieldError.getDefaultMessage())
                            .toList()
            );
        }

        if (currentUser == null) throw new UnauthorizedException("Devi essere autenticato");
        if (!currentUser.getRuolo().name().equals("ORGANIZZATORE")) {
            throw new UnauthorizedException("Solo un ORGANIZZATORE può creare eventi");
        }

        return eventiService.saveEvento(payload, currentUser);
    }

    @PutMapping("/{eventoId}")
    public Evento update(@PathVariable("eventoId") long eventoId,
                         @RequestBody @Valid EventoDTO payload,
                         BindingResult validationResult,
                         @AuthenticationPrincipal User currentUser) {

        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors().stream()
                            .map(fieldError -> fieldError.getDefaultMessage())
                            .toList()
            );
        }

        if (currentUser == null) throw new UnauthorizedException("Devi essere autenticato");
        if (!currentUser.getRuolo().name().equals("ORGANIZZATORE")) {
            throw new UnauthorizedException("Solo un ORGANIZZATORE può modificare eventi");
        }

        return eventiService.findByIdAndUpdate(eventoId, payload, currentUser);
    }

    @DeleteMapping("/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("eventoId") long eventoId,
                       @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) throw new UnauthorizedException("Devi essere autenticato");
        if (!currentUser.getRuolo().name().equals("ORGANIZZATORE")) {
            throw new UnauthorizedException("Solo un ORGANIZZATORE può eliminare eventi");
        }

        eventiService.findByIdAndDelete(eventoId, currentUser);
    }
}
