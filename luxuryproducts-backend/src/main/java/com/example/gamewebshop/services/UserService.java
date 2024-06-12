package com.example.gamewebshop.services;

import com.example.gamewebshop.Repositorys.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser customUser = userDAO.findByEmail(email);

        if (customUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Voeg hier een controle op de gebruikersrol toe, bijvoorbeeld:
        if (!customUser.getRole().equals("admin") && !customUser.getRole().equals("user")) {
            throw new IllegalStateException("User has invalid role: " + customUser.getRole());
        }

        return new User(
                email,
                customUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + customUser.getRole())));
    }

    public CustomUser validateUser(Principal principal){
        if (principal == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No user found"
            );
        }
        String userEmail = principal.getName();
        CustomUser user = userDAO.findByEmail(userEmail);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No user fount with that email"
            );
        }

        return user;
    }

}
