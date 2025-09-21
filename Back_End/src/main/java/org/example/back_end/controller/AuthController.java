package org.example.back_end.controller;

import org.example.back_end.dto.RegisterRequest;
import org.example.back_end.entity.Role;
import org.example.back_end.entity.User;
import org.example.back_end.repo.RoleRepository;
import org.example.back_end.repo.UserRepository;
import org.example.back_end.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:63342")  // allow your frontend
@RequestMapping("/auth")

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
// register user API

    @PostMapping("/register") //register endpoint http://localhost:8080/auth/register
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){

        //check if username alredy exists
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username is already in use");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(encodedPassword);
        System.out.println("Encoded password: " + encodedPassword);

        //convert role names role entities and assign  to user

        Set<Role> roles = new HashSet<>();
        for (String roleName : registerRequest.getRoles()) {
            Role role = roleRepository.findByRoleName(roleName).orElseThrow(()-> new RuntimeException("Role not found "+roleName));
            roles.add(role);

        }

        newUser.setRoles(roles);
        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");

    }

    //login API - //login endpoint http://localhost:8080/auth/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest){

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }catch (Exception e){
            System.out.println("Exception"+e);
        }
            String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);



    }

}
