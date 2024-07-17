package org.example.cyberzero.user;

import lombok.RequiredArgsConstructor;
import org.example.cyberzero.exceptions.InvalidPasswordException;
import org.example.cyberzero.exceptions.UserNotFoundException;
import org.example.cyberzero.security.JwtService;
import org.example.cyberzero.user.dto.*;
import org.example.cyberzero.user.entity.User;
import org.example.cyberzero.user.enumuration.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserTokenResponseDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Wrong password");
        }

        UserResponseDto response = mapper.toResponse(user);
        return jwtService.generate(response);
    }

    public UserTokenResponseDto register(UserRegisterDto registerDto) {
        User user = mapper.toUser(registerDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: %s".formatted(user.getEmail()));
        }

        User saved = userRepository.save(user);

        UserResponseDto userResponseDto = mapper.toResponse(saved);

        return jwtService.generate(userResponseDto);
    }

    public UserResponseDto me(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.toResponse(user);
    }

    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public UserResponseDto getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.toResponse(user);
    }

    public UserResponseDto updateUser(UUID id, UserUpdateDto updateDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        user.setFirstname(updateDto.getFirstname());
        user.setLastname(updateDto.getLastname());
        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }

    public void delete(UUID id) {
        userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: %s".formatted(id)));
        userRepository.deleteById(id);
    }
}
