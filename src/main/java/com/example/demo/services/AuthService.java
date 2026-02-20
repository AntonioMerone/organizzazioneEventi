package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.payloads.LoginDTO;
import com.example.demo.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsersService dipendentiService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UsersService dipendentiService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.dipendentiService = dipendentiService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    // Controllo credenziali e genero token
    public String checkCredentialAndGenerateToken(LoginDTO body) {
        //1 cerco l'utente tramite email
        User found = dipendentiService.findByEmail(body.email());
        //2 ora cambio, faccio il controllo per passworD, tra body e quella dell'utente.
        if(bcrypt.matches(body.password(), found.getPassword())){
            return jwtTools.generateToken(found);

        }

        // se la password è sbagliata mi da errore 401.
        throw new UnauthorizedException("Credenziali errate");
    }

}
