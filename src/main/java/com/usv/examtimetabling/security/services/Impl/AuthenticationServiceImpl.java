package com.usv.examtimetabling.security.services.Impl;

import com.usv.examtimetabling.config.DataConfiguration;
import com.usv.examtimetabling.security.dto.ChangePasswordDto;
import com.usv.examtimetabling.security.dto.JwtAuthenticationResponse;
import com.usv.examtimetabling.security.dto.RefreshTokenRequest;
import com.usv.examtimetabling.security.dto.SignInRequest;
import com.usv.examtimetabling.security.dto.SignUpRequest;
import com.usv.examtimetabling.security.services.AuthenticationService;
import com.usv.examtimetabling.security.services.JwtService;
import com.usv.examtimetabling.security.tokens.Tokens;
import com.usv.examtimetabling.security.tokens.TokensRepository;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.dto.CreateUserDto;
import com.usv.examtimetabling.user.model.dto.UsersDto;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.repository.UserRepository;
import com.usv.examtimetabling.user.student.model.Student;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final StudentRepository studentRepository;

    private final TokensRepository tokenRepository;

    private final JavaMailSender mailSender;

    private final DataConfiguration dataConfiguration;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    public UsersDto createUser(CreateUserDto createUserDto) {
        User user = User.builder()
            .name(createUserDto.getName())
            .email(createUserDto.getEmail())
            .password(passwordEncoder.encode(createUserDto.getPassword()))
            .role(Role.valueOf(createUserDto.getRole()))
            .created_at(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            .updated_at(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            .build();
        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.STUDENT || savedUser.getRole() == Role.GROUP_LEADER) {
            Student student = Student.builder()
                    .name(savedUser.getName())
                    .email(savedUser.getEmail())
                    .password(savedUser.getPassword())
                    .role(savedUser.getRole())
                    .year(1)
                    .build();
            studentRepository.save(student);
        }

        UsersDto usersDto = new UsersDto();
        usersDto.setName(savedUser.getName());
        usersDto.setEmail(savedUser.getEmail());
        usersDto.setRole(savedUser.getRole());

        return usersDto;
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));


        String token = UUID.randomUUID().toString();


        tokenRepository.save(Tokens.builder().email(email).token(token).build());

        String passwordResetUrl = "http://127.0.0.1:5173/reset-password?token=" + token;

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(dataConfiguration.getDefaultEmailSender());
            mailMessage.setTo(email);
            mailMessage.setSubject("Password Reset Request");
            mailMessage.setText("Hi "+ user.getName() +"\nTo reset your password, click the link below:\n" + passwordResetUrl);

            mailSender.send(mailMessage);
        } catch (Exception e) {
          logger.error("An error occurred while sending the email: {}", e.getMessage());
        }
    }

    @Override
    public void resetPassword(String token, String password) {
        Tokens tokens = tokenRepository.findByToken(token).orElseThrow();
        User user = userRepository.findByEmail(tokens.getEmail()).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        tokenRepository.delete(tokens);

        Student student = studentRepository.findByEmail(user.getEmail()).orElse(null);
        if (student != null && (student.getRole() == Role.STUDENT || student.getRole() == Role.GROUP_LEADER)) {
            student.setPassword(user.getPassword());
            studentRepository.save(student);
        }
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            String encodedNewPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
            user.setPassword(encodedNewPassword);
            userRepository.save(user);

            Student student = studentRepository.findByEmail(user.getEmail()).orElse(null);
            if (student != null && (student.getRole() == Role.STUDENT || student.getRole() == Role.GROUP_LEADER)) {
                student.setPassword(encodedNewPassword);
                studentRepository.save(student);
            }
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));
        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow
                (()-> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setEmail(user.getEmail());
        jwtAuthenticationResponse.setName(user.getName());
        jwtAuthenticationResponse.setRole(user.getRole());
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            jwtAuthenticationResponse.setRole(user.getRole());
            jwtAuthenticationResponse.setName(user.getName());
            jwtAuthenticationResponse.setEmail(user.getEmail());

            return jwtAuthenticationResponse;
        }

        return null;
    }

    @Override
    public UsersDto signUp(SignUpRequest signUpRequest) {
        User user =  userRepository.save(User.builder()
            .email(signUpRequest.getEmail())
            .name(signUpRequest.getName())
            .role(Role.ADMIN)
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .build());

        return UsersDto.builder()
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole())
            .build();
    }
}
