package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.model.RestaurantTableStatus;
import com.example.bookingrestaurant.repositories.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service responsável por receber os dados passados pelo RestaurantTableController e BookingService
 * sendo possível criar, salvar, obter, modificar e deletar os dados das mesas diretamente com o banco.
 * Possui um atributo para obtenção de dados: RestaurantTableRepository.
 * Lança as exceções relacionadas com as Mesas de Restaurante.
 */
@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository repository;

    // TODO fazer melhores Exceptions

    /**
     * Method responsável por procurar a mesa de acordo com o id passado.
     * Retorna a mesa ou uma exceção de Mesa não Encontrada
     */
    public RestaurantTable findTableById(Long id) throws Exception {
        return repository.findRestaurantTableById(id)
                         .orElseThrow(() -> new Exception("Mesa de Restaurante não encontrada"));
    }

    /**
     * Method responsável por criar uma nova mesa.
     * Recebe os dados passados pelo RestaurantTableController.
     * Salva a nova mesa no banco de dados e retorna ela.
     */
    public RestaurantTable createRestaurantTable(RestaurantTableDTO data){
        RestaurantTable newTable = new RestaurantTable(data);
        this.saveRestaurantTable(newTable);
        return newTable;
    }

    /**
     * Method privado responsável por salvar a mesa no banco de dados.
     * Recebe os dados de uma mesa passada e faz o commit dela.
     */
    private void saveRestaurantTable(RestaurantTable table){
        repository.save(table);
    }

    /**
     * Method responsável por retornar todas as mesas do banco.
     * Retorna as mesas para o RestaurantTableController.
     */
    public List<RestaurantTable> getAllTables() {
        return repository.findAll();
    }

    /**
     * Method responsável por deletar a mesa no banco.
     * Não retorna a mesa.
     */
    public void deleteTable(RestaurantTable table) {
        repository.delete(table);
    }

    /**
     * Method responsável por atualizar os dados de uma mesa.
     * Recebe o id da mesa e os dados para serem atualizados do RestaurantTableController.
     * Checa se os parâmetros são nulos, e insere os dados atualizados na mesa.
     * Retorna a mesa modificada para o Controller.
     */
    public RestaurantTable updateTable(Long id, RestaurantTableDTO updates) throws Exception {
        RestaurantTable table = this.findTableById(id);

        if(updates.name() != null){
            table.setName(updates.name());
        }

        if(updates.capacity() > 0){
            table.setCapacity(updates.capacity());
        }

        if(updates.status() != null){
            table.setStatus(updates.status());
        }

        this.saveRestaurantTable(table);

        return table;
    }

    /**
     * Method responsável por checar a validação do status de uma mesa.
     * Recebe a mesa que precisa ser checada.
     * Verifica se a mesa tem o status de BOOKED(reservada) ou INACTIVE(inativa),
     * caso seja, joga uma Exceção de Mesa Inválida.
     */
    public void checkTableValidation(RestaurantTable table) throws Exception {
        boolean tableBooked = table.getStatus() == RestaurantTableStatus.BOOKED;
        boolean tableInactive = table.getStatus() == RestaurantTableStatus.INACTIVE;
        boolean invalidTable = tableBooked || tableInactive;

        if(invalidTable) {
            throw new Exception("Mesa inválida");
        }
    }
}