package com.example.demo.services;


import com.example.demo.entities.User;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.payloads.UserDTO;
import com.example.demo.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder brcypt;


    public UsersService(UsersRepository usersRepository, PasswordEncoder brcypt) {
        this.usersRepository = usersRepository;
        this.brcypt = brcypt;
    }

    //save
    public User saveUser(UserDTO payload) {
//fare check
        User user = new User(
                payload.email(),
                brcypt.encode(payload.password()),
                payload.ruolo()
        );
        return usersRepository.save(user);

    }


    public User findById(long userId){
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("il dipendente con l'id" + userId + "non è stato trovato"));
    }

    public User findByEmail(String email){
        return this.usersRepository
                .findByEmail(email)
                .orElseThrow(()->  new UnauthorizedException("credenziali errate"));
    }
}
