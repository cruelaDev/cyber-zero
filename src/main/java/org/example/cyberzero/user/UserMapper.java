package org.example.cyberzero.user;

import org.example.cyberzero.user.dto.UserRegisterDto;
import org.example.cyberzero.user.dto.UserResponseDto;
import org.example.cyberzero.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract User toUser(UserRegisterDto registerDto);

    public abstract UserResponseDto toResponse(User user);
//    public abstract void toUser(UserUpdateDto updateDto);
}
