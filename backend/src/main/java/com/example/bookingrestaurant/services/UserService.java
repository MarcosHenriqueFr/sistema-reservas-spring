package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.exception.BookingException;
import com.example.bookingrestaurant.config.exception.UserAlreadyExistsException;
import com.example.bookingrestaurant.config.security.config.SecurityConfig;
import com.example.bookingrestaurant.config.security.userdetails.UserAuthenticated;
import com.example.bookingrestaurant.config.security.userdetails.UserDetailsServiceImpl;
import com.example.bookingrestaurant.dto.UserPostDTO;
import com.example.bookingrestaurant.model.RoleName;
import com.example.bookingrestaurant.model.User;
import com.example.bookingrestaurant.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service responsável por receber os dados passados pelo AuthenticationController e BookingService,
 * sendo possível criar, salvar e obter os dados dos usuários diretamente com o banco.
 * Possui dois atributos para obtenção de dados: UserRepository e UserDetailsService.
 * Possui um atributo para encriptar os dados: SecurityConfig.
 * Lança as exceções relacionadas com o Usuário.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Metodo responsável obter o usuário a partir do elemento único Email.
     * Recebe os dados de Email do BookingService.
     * Joga uma Exceção caso o Usuário não tenha sido encontrado no banco de dados.
     */
    public UserAuthenticated getUserByEmail(String email) throws UsernameNotFoundException {
        return (UserAuthenticated) userDetailsService.loadUserByUsername(email);
    }

    /**
     * Metodo responsável por checar se um usuário já existe no sistema,
     * e retorna um booleano indicando se o email pode ser usado.
     */
    private boolean isUserAvailable(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return user == null;
    }

    /**
     * Metodo responsável por persistir um usuário no banco de dados.
     * Recebe os dados do novo usuário a partir do AuthenticationController
     */
    public User createUser(UserPostDTO dto) throws UserAlreadyExistsException {
        String email = dto.email();

        if(!this.isUserAvailable(email)){
            throw new UserAlreadyExistsException("O email já existe no sistema");
        }

        String name = dto.name();
        String password = securityConfig.passwordEncoder().encode(dto.password());
        RoleName role = dto.role();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        this.saveUser(user);
        logger.info("Usuário criado com sucesso.");

        return user;
    }

    /**
     * Metodo privado responsável por salvar o usuário no banco de dados.
     * Recebe os dados do usuário passado e faz o commit dele.
     */
    private void saveUser(User user){
        userRepository.save(user);
        logger.info("Usuário salvo no banco de dados.");
    }
}
