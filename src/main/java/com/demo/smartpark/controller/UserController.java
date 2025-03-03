package com.demo.smartpark.controller;

import com.demo.smartpark.exception.ApplicationException;
import com.demo.smartpark.mapper.UserMapper;
import com.demo.smartpark.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.demo.smartpark.dto.UserDto;

import java.util.Optional;

/**
 * @author jandrada
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {

        return Optional.ofNullable(userDto)
                .map(userMapper::userDtoToUser)
                .map(userService::register)
                .map(userMapper::userToUserDto)
                .map(ResponseEntity::ok)
                .orElseThrow(ApplicationException::new);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto) {

        return userService.verifyCredentials(userDto);
    }
}
