package com.example.bookingrestaurant.config.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

/**
 * Service dedicado para a criação do token JWT,
 * incluindo suas datas de criação e expiração, ou seja, toda a lógica de criação do token.
 * É usado diretamente pela classe de AuthenticationService.
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final JwtEncoder encoder;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public JwtService(JwtEncoder encoder){
        this.encoder = encoder;
    }

    /**
     * Metodo responsável por toda a criação do JWT token.
     * Recebe uma Authentication com as credenciais do usuário.
     * Delimita o Issuer, a data de criação e expiração,
     * o email do Usuário e os papéis dele no token.
     * Retorna uma string com os dados separados dentro do token.
     */
    public String generateToken(Authentication authentication){
        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(creationDate())
                .expiresAt(expirationDate())
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();

        logger.info("Token gerado para o usuário {}.", authentication.getName());
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Metodo privado dedicado para a data de inicio do token.
     */
    private Instant creationDate(){
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }

    /**
     * Metodo privado dedicado para a data de fim do token.
     * Retorna o tempo de vida do token, sendo de 2 horas.
     */
    private Instant expirationDate(){
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(2).toInstant();
    }
}
