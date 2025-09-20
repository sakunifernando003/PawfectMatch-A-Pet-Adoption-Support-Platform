package org.example.back_end.service;

import org.example.back_end.entity.User;
import org.example.back_end.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class UserDetailService implements UserDetailsService {


   private final UserRepository userRepository;

   public UserDetailService(UserRepository userRepository) {
       this.userRepository = userRepository;

   }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"+username));

       //map the roles to authority
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())) ;
    }
}
