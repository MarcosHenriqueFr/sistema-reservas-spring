package com.example.bookingrestaurant.repositories;

import com.example.bookingrestaurant.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository responsável por interagir com os dados da tabela de 'restaurant_tables',
 * É possível receber uma mesa de restaurante a partir do seu id.
 */
@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findRestaurantTableById(Long id);
}
