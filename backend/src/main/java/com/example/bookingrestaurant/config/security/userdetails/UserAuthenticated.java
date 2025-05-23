package com.example.bookingrestaurant.config.security.userdetails;

import com.example.bookingrestaurant.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Classe dedicada para representar a classe de usuário e obter suas informações.
 * Usada em BookingService, UserService e UserDetailsImpl.
 * É uma classe que encapsula as informações do usuário e permite maior controle sobre ele.
 */
public class UserAuthenticated implements UserDetails {

    private final User user;

    public UserAuthenticated(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Tem relação com o atributo único da tabela do banco
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
