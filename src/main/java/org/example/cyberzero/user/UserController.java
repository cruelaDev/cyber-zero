package org.example.cyberzero.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cyberzero.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    UserTokenResponseDto login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping("/register")
    UserTokenResponseDto register(@RequestBody @Valid UserRegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @GetMapping("/me")
    ResponseEntity<UserResponseDto> me(Principal principal) {
        UserResponseDto me = userService.me(principal);
        return ResponseEntity.ok(me);
    }

    @GetMapping
    ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        UserResponseDto responseDto = userService.getUser(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable UUID id,
                                                  @RequestBody @Valid UserUpdateDto updateDto) {
        UserResponseDto responseDto = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
