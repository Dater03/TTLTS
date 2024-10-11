package com.example.ttlts.exception;

import com.example.ttlts.dto.request.UserRequest;
import com.example.ttlts.dto.response.UserResponse;
import com.example.ttlts.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
}
