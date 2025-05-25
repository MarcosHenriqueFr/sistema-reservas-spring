package com.example.bookingrestaurant.services;

import com.example.bookingrestaurant.config.security.config.SecurityConfig;
import com.example.bookingrestaurant.config.security.userdetails.UserAuthenticated;
import com.example.bookingrestaurant.config.security.userdetails.UserDetailsServiceImpl;
import com.example.bookingrestaurant.dto.UserPostDTO;
import com.example.bookingrestaurant.model.RoleName;
import com.example.bookingrestaurant.model.User;
import com.example.bookingrestaurant.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Method responsável obter o usuário a partir do elemento único Email.
     * Recebe os dados de Email do BookingService.
     * Joga uma Exceção caso o Usuário não tenha sido encontrado no banco de dados.
     */
    public UserAuthenticated getUserByEmail(String email) throws UsernameNotFoundException {
        return (UserAuthenticated) userDetailsService.loadUserByUsername(email);
    }

    /**
     * Method responsável por persistir um usuário no banco de dados.
     * Recebe os dados do novo usuário a partir do AuthenticationController
     */
    public User createUser(UserPostDTO dto) {
        String name = dto.name();
        String email = dto.email();
        String password = securityConfig.passwordEncoder().encode(dto.password());
        RoleName role = dto.role();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        this.saveUser(user);

        return user;
    }

    /**
     * Method privado responsável por salvar o usuário no banco de dados.
     * Recebe os dados do usuário passado e faz o commit dele.
     */
    private void saveUser(User user){
        userRepository.save(user);
    }
}
