package com.example.demo.services;


import com.example.demo.entities.User;
import com.example.demo.payloads.UserDTO;
import com.example.demo.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
