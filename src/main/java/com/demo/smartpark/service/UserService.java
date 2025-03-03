package com.demo.smartpark.service;

import com.demo.smartpark.entity.User;
import com.demo.smartpark.dto.UserDto;
import com.demo.smartpark.exception.ApplicationException;
import com.demo.smartpark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author jandrada
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    // Set the strength to only 10 to avoid unnecessary application performance demand.
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public User register(User user) {

        // use the encoder for the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return Optional.of(user)
                .map(userRepository::save)
                .orElseThrow(ApplicationException::new);
    }

    public ResponseEntity<String> verifyCredentials(UserDto userDto) {
        try {
            // Authenticate the user
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword())
            );

            // If authenticated, generate and return the jwt token
            if (auth.isAuthenticated()) {
                String token = jwtService.generateToken(userDto.getUserName());
                return ResponseEntity.ok(token);
            }

            // If not authenticated.
            return ResponseEntity.badRequest().body("Login failed! Invalid credentials.");

        } catch (BadCredentialsException e) {
            // Specific case for bad credentials but throw same error.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed! Invalid credentials.");
        } catch (Exception e) {
            // General error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred. Please try again later.");
        }
    }
}
