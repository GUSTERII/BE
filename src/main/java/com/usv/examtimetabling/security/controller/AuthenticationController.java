package com.usv.examtimetabling.security.controller;

import com.usv.examtimetabling.security.dto.JwtAuthenticationResponse;
import com.usv.examtimetabling.security.dto.RefreshTokenRequest;
import com.usv.examtimetabling.security.dto.SignInRequest;
import com.usv.examtimetabling.security.dto.SignUpRequest;
import com.usv.examtimetabling.security.services.AuthenticationService;
import com.usv.examtimetabling.user.model.dto.CreateUserDto;
import com.usv.examtimetabling.user.model.dto.UsersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(authenticationService.createUser(createUserDto));
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/sign-up")
    public UsersDto signUp(@RequestBody SignUpRequest signUpRequest) {
        return authenticationService.signUp(signUpRequest);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email) {
        authenticationService.forgotPassword(email);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }


    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String password) {
        authenticationService.resetPassword(token, password);
    }
}
