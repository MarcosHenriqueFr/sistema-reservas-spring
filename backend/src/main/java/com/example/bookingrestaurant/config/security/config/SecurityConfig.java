package com.example.bookingrestaurant.config.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String ADMIN_ROLE = "ADMINISTRATOR";
    private final String CUSTOMER_ROLE = "CUSTOMER";

    @Value("${jwt.public.key}")
    private RSAPublicKey key;

    @Value("${jwt.private.key}")
    private RSAPrivateKey priv;

    private final String[] ENDPOINT_WITHOUT_AUTH = {
            "/usuarios/login",
            "/usuarios/registrar"
    };

    private final String[] TABLE_ENDPOINTS = {
            "/mesas",
            "/mesas/:{id}"
    };

    private final String[] BOOKING_ENDPOINTS = {
            "/reservas",
            "/reservas/:{id}/cancelar"
    };

    // O mét0do que controla toda a segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(HttpMethod.POST, ENDPOINT_WITHOUT_AUTH).permitAll()
                                        .requestMatchers(HttpMethod.POST, TABLE_ENDPOINTS).hasRole(ADMIN_ROLE)
                                        .requestMatchers(HttpMethod.GET, TABLE_ENDPOINTS).hasAnyRole(ADMIN_ROLE, CUSTOMER_ROLE)
                                        .requestMatchers(HttpMethod.PATCH, TABLE_ENDPOINTS).hasAnyRole(ADMIN_ROLE, CUSTOMER_ROLE)
                                        .requestMatchers(HttpMethod.DELETE, TABLE_ENDPOINTS).hasRole(ADMIN_ROLE)
                                        .requestMatchers(BOOKING_ENDPOINTS).authenticated()
                                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(
                        conf -> conf.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthorities = new JwtGrantedAuthoritiesConverter();
        grantedAuthorities.setAuthoritiesClaimName("scope");
        grantedAuthorities.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthorities);
        return authenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        RSAKey jwk = new RSAKey.Builder(key).privateKey(priv).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
