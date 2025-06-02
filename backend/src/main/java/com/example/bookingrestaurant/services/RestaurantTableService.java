package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.exception.InvalidRestaurantTableException;
import com.example.bookingrestaurant.config.exception.RestaurantTableNotFoundException;
import com.example.bookingrestaurant.dto.RestaurantTableDTO;
import com.example.bookingrestaurant.model.RestaurantTable;
import com.example.bookingrestaurant.model.RestaurantTableStatus;
import com.example.bookingrestaurant.repositories.RestaurantTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsável por receber os dados passados pelo RestaurantTableController e BookingService
 * sendo possível criar, salvar, obter, modificar e deletar os dados das mesas diretamente com o banco.
 * Possui um atributo para obtenção de dados: RestaurantTableRepository.
 * Lança as exceções relacionadas com as Mesas de Restaurante.
 */
@Service
public class RestaurantTableService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableService.class);

    @Autowired
    private RestaurantTableRepository repository;

    /**
     * Metodo responsável por procurar a mesa de acordo com o id passado.
     * Retorna a mesa ou uma exceção de Mesa não Encontrada
     */
    public RestaurantTable findTableById(Long id) throws RestaurantTableNotFoundException {
        return repository.findRestaurantTableById(id)
                         .orElseThrow(() -> new RestaurantTableNotFoundException("Mesa de Restaurante não encontrada"));
    }

    /**
     * Metodo responsável por criar uma nova mesa.
     * Recebe os dados passados pelo RestaurantTableController.
     * Salva a nova mesa no banco de dados e retorna ela.
     */
    public RestaurantTable createRestaurantTable(RestaurantTableDTO data, String email){
        RestaurantTable newTable = new RestaurantTable(data);

        this.saveRestaurantTable(newTable);
        logger.info("Mesa de id {} criada pelo usuário {} com sucesso.", newTable.getId(), email);

        return newTable;
    }

    /**
     * Metodo privado responsável por salvar a mesa no banco de dados.
     * Recebe os dados de uma mesa passada e faz o commit dela.
     */
    private void saveRestaurantTable(RestaurantTable table){
        repository.save(table);
        logger.info("Mesa de id {} salva no banco de dados", table.getId());
    }

    /**
     * Metodo responsável por retornar todas as mesas do banco.
     * Retorna as mesas para o RestaurantTableController.
     */
    public List<RestaurantTable> getAllTables(String email) {
        logger.info("Todas as mesas foram selecionadas pelo usuário com email {}.", email);
        return repository.findAll();
    }

    /**
     * Metodo responsável por deletar a mesa no banco.
     * Faz uma verificação inicial, para evitar que uma mesa seja apagada se estiver em uso.
     * Não retorna a mesa.
     */
    public void deleteTable(RestaurantTable table, String email) throws InvalidRestaurantTableException {
        checkTableValidation(table);

        repository.delete(table);
        logger.info("Mesa de id {} apagada do banco de dados pelo usuário {}.", table.getId(), email);
    }

    /**
     * Metodo responsável por atualizar os dados de uma mesa.
     * Recebe o id da mesa e os dados para serem atualizados do RestaurantTableController.
     * Checa se os parâmetros são nulos, e insere os dados atualizados na mesa.
     * Retorna a mesa modificada para o Controller.
     */
    public RestaurantTable updateTable(Long id, RestaurantTableDTO updates, String email) throws RestaurantTableNotFoundException, InvalidRestaurantTableException {
        RestaurantTable table = this.findTableById(id);
        checkTableValidation(table);

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
        logger.info("Mesa de id {} modificada pelo usuário {} com sucesso.", table.getId(), email);

        return table;
    }

    /**
     * Metodo responsável por checar a validação do status de uma mesa.
     * Recebe a mesa que precisa ser checada.
     * Verifica se a mesa tem o status de BOOKED(reservada) ou INACTIVE(inativa),
     * caso seja, joga uma Exceção de Mesa Inválida.
     */
    public void checkTableValidation(RestaurantTable table) throws InvalidRestaurantTableException {
        boolean tableBooked = table.getStatus() == RestaurantTableStatus.BOOKED;
        boolean tableInactive = table.getStatus() == RestaurantTableStatus.INACTIVE;
        boolean invalidTable = tableBooked || tableInactive;

        if(invalidTable) {
            throw new InvalidRestaurantTableException("Mesa Inativa ou Reservada");
        }
    }
}