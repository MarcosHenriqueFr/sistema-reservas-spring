package com.example.bookingrestaurant.config.security.auth;

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

@Service
public class JwtService {
    private final JwtEncoder encoder;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public JwtService(JwtEncoder encoder){
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication){

        // TODO melhorar a documentaçãp
        // Passa tambem os papeis dentro do token
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

        // Converte as Claims em uma String
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Instant creationDate(){
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }

    private Instant expirationDate(){
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(2).toInstant();
    }
}
