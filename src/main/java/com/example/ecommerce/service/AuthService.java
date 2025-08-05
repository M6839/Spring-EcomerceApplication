package com.example.ecommerce.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.config.JwtUtil;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(RegisterRequest request) {
        boolean userExists = userRepository.findByUsername(request.getUsername()).isPresent();

        if (userExists) {
            return "Username already taken!";
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = request.getRole();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of(Role.USER);
        }
        newUser.setRole(roles);

        userRepository.save(newUser);

        return "User registered successfully!";
    }
    
    public String authenticateAndGenerateToken(String username,String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }

}
