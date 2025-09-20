package org.example.back_end.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.back_end.entity.Role;
import org.example.back_end.entity.User;
import org.example.back_end.repo.UserRepository;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class JwtUtil {

    //secret key

    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    // expiration time
    private final int jwtExpirationInMinutes = 86400000;

    private UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Generate token
    public String generateToken(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Set<Role> roles = user.get().getRoles();

        // Add roles through the token

    return Jwts.builder().setSubject(username).claim("roles" , roles.stream()
            .map(role -> role.getName()).collect(Collectors.joining(",")))
            .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + jwtExpirationInMinutes))
            .signWith(secretKey).compact();

    }
    //extract username
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();

    }

    //extract roles
    public Set<String> extractRoles(String token) {
        String rolesString = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody().get("roles" , String.class);
        return Set.of(rolesString);
    }

    //Token validation

    public boolean isTokenValid(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;

        }catch (JwtException | IllegalArgumentException e){
            return false;
        }

    }





}

