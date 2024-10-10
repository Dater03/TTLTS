package com.example.ttlts.mapper;

import com.example.ttlts.dto.request.UserRequest;
import com.example.ttlts.dto.response.UserResponse;
import com.example.ttlts.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface IUserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserRequest userRequest);
}
