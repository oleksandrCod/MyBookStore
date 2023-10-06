package store.mybookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybookstore.dto.user.UserRegistrationRequestDto;
import store.mybookstore.dto.user.UserRegistrationResponseDto;
import store.mybookstore.dto.user.records.UserLoginRequestDto;
import store.mybookstore.dto.user.records.UserLoginResponseDto;
import store.mybookstore.exception.RegistrationException;
import store.mybookstore.security.AuthenticationService;
import store.mybookstore.service.user.UserService;

@Tag(name = "Account management", description = "Endpoints for login and registration")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login endpoint",
            description = "Allows the user to log into the account")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @Operation(summary = "Register endpoint",
            description = "Allows the user to register new account")
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
