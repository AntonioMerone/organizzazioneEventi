package com.example.demo.repositories;

import com.example.demo.entities.Evento;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventiRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByUserOrganizzatoreEvento(User OrganizzatoreEvento);

}
