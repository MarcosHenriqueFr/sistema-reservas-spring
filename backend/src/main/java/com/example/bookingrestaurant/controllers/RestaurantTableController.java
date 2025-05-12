package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.repositories.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "mesas")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables(){
        List<RestaurantTable> tables = restaurantTableRepository.findAll();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<RestaurantTable> createTable(@RequestBody RestaurantTableDTO data){
        RestaurantTable newTable = new RestaurantTable(data);
        restaurantTableRepository.save(newTable);
        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }
}
