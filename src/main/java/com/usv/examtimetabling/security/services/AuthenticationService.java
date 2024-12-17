package com.usv.examtimetabling.security.services;


import com.usv.examtimetabling.security.dto.ChangePasswordDto;
import com.usv.examtimetabling.security.dto.JwtAuthenticationResponse;
import com.usv.examtimetabling.security.dto.RefreshTokenRequest;
import com.usv.examtimetabling.security.dto.SignInRequest;
import com.usv.examtimetabling.security.dto.SignUpRequest;
import com.usv.examtimetabling.user.model.dto.CreateUserDto;
import com.usv.examtimetabling.user.model.dto.UsersDto;

public interface AuthenticationService {
    UsersDto createUser(CreateUserDto createUserDto);

    UsersDto signUp(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signIn(SignInRequest signInRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void forgotPassword(String email);

    void resetPassword(String token, String password);

    void changePassword(ChangePasswordDto changePasswordDto);
}
