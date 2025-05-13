package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "mesas")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables(){
        List<RestaurantTable> tables = tableService.getAllTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestaurantTable> createTable(@RequestBody RestaurantTableDTO data){
        RestaurantTable newTable = tableService.createRestaurantTable(data);
        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }

    // TODO Fazer o tratamento de Exceções/Criacao -> No delete e no patch

    @PatchMapping(path = ":{id}")
    public ResponseEntity<RestaurantTable> updateTable(@PathVariable Long id, @RequestBody RestaurantTableDTO updates) throws Exception {
        RestaurantTable modifiedTable = tableService.updateTable(id, updates);
        return new ResponseEntity<>(modifiedTable, HttpStatus.OK);
    }


    @DeleteMapping(path = ":{id}")
    public ResponseEntity<RestaurantTable> deleteTable(@PathVariable Long id) throws Exception {
        RestaurantTable table = tableService.findTableById(id);
        tableService.deleteTable(table);
        return new ResponseEntity<>(table, HttpStatus.NO_CONTENT);
    }
}
