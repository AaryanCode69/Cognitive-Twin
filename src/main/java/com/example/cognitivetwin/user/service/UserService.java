package com.example.cognitivetwin.user.service;

import com.example.cognitivetwin.mapper.UserMapper;
import com.example.cognitivetwin.exception.custom.EmailAlreadyExistsException;
import com.example.cognitivetwin.user.Role;
import com.example.cognitivetwin.user.dto.Request.UserRequestDTO;
import com.example.cognitivetwin.user.dto.Response.UserResponseDTO;
import com.example.cognitivetwin.user.entity.UserEntity;
import com.example.cognitivetwin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO req){
        log.info("Registering New User");
        if (userRepository.existsByEmail(req.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        UserEntity userEntity = userMapper.mapUserRequestToUser(req);
        userEntity.setUserRole(Role.CUSTOMER);
        userEntity.setPassword(passwordEncoder.encode(req.password()));
        try{
            userEntity = userRepository.save(userEntity);
        }catch (DataIntegrityViolationException ex){
            log.warn("Duplicate email detected during save", ex);
            throw new EmailAlreadyExistsException("Email already exists");
        }
        log.info("User registration successful");
        return userMapper.mapUserToUserResponse(userEntity);
    }

}
