package org.example.back_end.controller;

import org.example.back_end.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:63342")  // allow your frontend
@RequestMapping("/api")

public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${role.admin}")
    private String roleAdmin;

    @Value("${role.user}")
    private String roleUser;

    // endpoint to access user protected resources

    @GetMapping("/protected-data")
    public ResponseEntity<String> protectedData(@RequestHeader("Authorization") String token) {

        if(token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); //remove bearer prefix

            try {
                if (jwtUtil.isTokenValid(jwtToken)){
                    String username = jwtUtil.extractUsername(jwtToken); //extract username from the jwtToken

                    //extract roles from jwt Token

                    Set<String> roles = jwtUtil.extractRoles(jwtToken);

                    if (roles.contains(roleAdmin)){
                        return ResponseEntity.ok("Welcome "+ username+" here is the " + roles+"-specific data");

                    }else if (roles.contains(roleUser)){
                        return ResponseEntity.ok("Welcome "+ username+" here is the " + roles+"-specific data");

                    }
                    else {
                        return ResponseEntity.status(403).body("Access denied: no role");

                    }

                }
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");

    }




}
