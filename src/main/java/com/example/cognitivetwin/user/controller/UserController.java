package com.example.cognitivetwin.user.controller;

import com.example.cognitivetwin.user.dto.Request.UserRequestDTO;
import com.example.cognitivetwin.user.dto.Response.UserResponseDTO;
import com.example.cognitivetwin.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request){
        log.info("Received user registration request");
        UserResponseDTO userResponseDTO = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }
}
