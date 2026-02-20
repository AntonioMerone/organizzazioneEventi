package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.exceptions.ValidationException;
import com.example.demo.payloads.LoginDTO;
import com.example.demo.payloads.LoginResponseDTO;
import com.example.demo.payloads.UserDTO;
import com.example.demo.services.AuthService;
import com.example.demo.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsersService usersService;

    public AuthController(AuthService authService, UsersService usersService) {
        this.authService = authService;
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO body) {
        String token = authService.checkCredentialAndGenerateToken(body);
        //1 RICEVO EMAIL E PASSWORD DAL CLIENT
        //2 DELEGO AL SERVICE: CERCO UTENTE, VERIFICO CREDENZIALI E GENERO UN JWT
        return new LoginResponseDTO(token);
        //poi 3 ritorno il token al client, il client lo usa nella request protette e poi
        //fa authorization bearer <token>
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid UserDTO payload,
                         BindingResult validationResult) {
        //qua facciamo validazione manuale; @valid popola validationresult con eventuali errori
        //se ci sono errori, trasformo gli errori in lista di stringhe.
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();
            //qua poi lancio eccezione custom
            throw new ValidationException(errors);
        }
        //salvo utente nel db
        return this.usersService.saveUser(payload);
    }
}