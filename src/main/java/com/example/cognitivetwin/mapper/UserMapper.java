package com.example.cognitivetwin.mapper;

import com.example.cognitivetwin.user.dto.Request.UserRequestDTO;
import com.example.cognitivetwin.user.dto.Response.UserResponseDTO;
import com.example.cognitivetwin.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO mapUserToUserResponse(UserEntity user);

    @Mapping(target = "password",ignore = true)
    UserEntity mapUserRequestToUser(UserRequestDTO userRequestDTO);
}
