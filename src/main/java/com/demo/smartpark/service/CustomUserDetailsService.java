package com.demo.smartpark.service;

import com.demo.smartpark.dto.CurrentUserDto;
import com.demo.smartpark.entity.User;
import com.demo.smartpark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author jandrada
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.of(userRepository.findByUserName(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return new CurrentUserDto(user);
    }
}
