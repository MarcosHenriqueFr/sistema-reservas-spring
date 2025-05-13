package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.repositories.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository repository;

    // TODO fazer melhores Exceptions

    public RestaurantTable findTableById(Long id) throws Exception {
        return repository.findRestaurantTableById(id)
                         .orElseThrow(() -> new Exception("Mesa de Restaurante não encontrada"));
    }

    public RestaurantTable createRestaurantTable(RestaurantTableDTO data){
        RestaurantTable newTable = new RestaurantTable(data);
        this.saveRestaurantTable(newTable);
        return newTable;
    }

    public void saveRestaurantTable(RestaurantTable table){
        repository.save(table);
    }

    public List<RestaurantTable> getAllTables() {
        return repository.findAll();
    }

    public void deleteTable(RestaurantTable table) {
        repository.delete(table);
    }

    public RestaurantTable updateTable(Long id, RestaurantTableDTO updates) throws Exception {
        RestaurantTable table = this.findTableById(id);

        if(updates.name() != null){
            table.setName(updates.name());
        }

        if(updates.capacity() != 0){
            table.setCapacity(updates.capacity());
        }

        if(updates.status() != null){
            table.setStatus(updates.status());
        }

        this.saveRestaurantTable(table);

        return table;
    }
}