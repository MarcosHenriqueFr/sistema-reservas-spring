package com.example.bookingrestaurant.controllers;

import com.example.bookingrestaurant.config.exception.InvalidRestaurantTableException;
import com.example.bookingrestaurant.config.exception.RestaurantTableNotFoundException;
import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.services.RestaurantTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsável pela rota de /mesas,
 * tem como principal responsabilidade enviar os dados para a camada de RestaurantTableService
 * para a criação, obtenção, atualização e destruição dos dados das mesas do Restaurante.
 * Esse Controller aceita somente as requisições do tipo POST, GET, PATCH e DELETE.
 */
@RestController
@RequestMapping(path = "mesas")
@Validated
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService tableService;

    /**
     * Mapping responsável pela requisição do tipo GET na rota /mesas.
     * Não recebe nenhum parâmetro e retorna todas as mesas do restaurante,
     * independente do seu status.
     */
    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables(){
        List<RestaurantTable> tables = tableService.getAllTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    /**
     * Mapping responsável pela requisição do tipo POST na rota de /mesas
     * Recebe os dados da nova mesa a ser criada.
     * Retorna a nova mesa caso tenha sido criada e o status Http OK
     */
    @PostMapping
    public ResponseEntity<RestaurantTable> createTable(@Valid @RequestBody RestaurantTableDTO data){
        RestaurantTable newTable = tableService.createRestaurantTable(data);
        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }

    // TODO Fazer o tratamento de Exceções/Criacao -> No delete e no patch

    /**
     * Mapping responsável pela requisição do tipo PATCH na rota de /mesas/:{id}.
     * Recebe os dados do id da RestaurantTable e os dados a serem modificados.
     * Retorna a mesa modificada e o status Http OK
     */
    @PatchMapping(path = ":{id}")
    public ResponseEntity<RestaurantTable> updateTable(@PathVariable Long id, @Valid @RequestBody RestaurantTableDTO updates) throws Exception {
        RestaurantTable modifiedTable = tableService.updateTable(id, updates);
        return new ResponseEntity<>(modifiedTable, HttpStatus.OK);
    }

    /**
     * Mapping responsável pela requisição do tipo DELETE na rota de /mesas/:{id}.
     * Recebe os dados de id da RestaurantTable.
     * Retorna a mesa que foi excluida, ou seja, null e o status Http de NO CONTENT
     */
    @DeleteMapping(path = ":{id}")
    public ResponseEntity<RestaurantTable> deleteTable(@PathVariable Long id) throws RestaurantTableNotFoundException, InvalidRestaurantTableException {
        RestaurantTable table = tableService.findTableById(id);
        tableService.deleteTable(table);
        return new ResponseEntity<>(table, HttpStatus.NO_CONTENT);
    }
}
