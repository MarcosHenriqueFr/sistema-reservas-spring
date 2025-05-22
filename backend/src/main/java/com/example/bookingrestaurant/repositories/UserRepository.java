package com.example.bookingrestaurant.repositories;

import com.example.bookingrestaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository responsável por interagir com os dados da tabela de 'users',
 * É possível receber um usuário a partir de seu atributo único(email).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
