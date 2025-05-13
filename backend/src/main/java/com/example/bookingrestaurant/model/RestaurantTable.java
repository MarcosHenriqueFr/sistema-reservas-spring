package com.example.bookingrestaurant.model;

import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private RestaurantTableStatus restaurantTableStatus;

    public RestaurantTable(){

    }

    public RestaurantTable(RestaurantTableDTO data){
        this.name = data.name();
        this.capacity = data.capacity();
        this.restaurantTableStatus = RestaurantTableStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RestaurantTableStatus getStatus() {
        return restaurantTableStatus;
    }

    public void setStatus(RestaurantTableStatus restaurantTableStatus) {
        this.restaurantTableStatus = restaurantTableStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTable that = (RestaurantTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
