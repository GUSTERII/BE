package com.usv.examtimetabling.user.service.impl;

import com.usv.examtimetabling.materie.repository.MaterieRepository;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.dto.UpdateUserDto;
import com.usv.examtimetabling.user.model.dto.UsersDto;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.profesor.ProfesorRepository;
import com.usv.examtimetabling.user.repository.UserRepository;
import com.usv.examtimetabling.user.service.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetailsService userDetailsService() {
    return email ->
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public List<UsersDto> getAll() {
    return userRepository.findAll().stream()
        .map(
            user ->
                UsersDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .created_at(user.getCreated_at())
                    .updated_at(user.getUpdated_at())
                    .build())
        .toList();
  }

  public User getUserByEmail(String email) {
    return getUserEntityByEmail(email);
  }

  public User updateUserByEmail(String email, UpdateUserDto updateUserDto) {
    User user = getUserEntityByEmail(email);
    user.setName(updateUserDto.getName());
    user.setRole(updateUserDto.getRole());
    user.setUpdated_at(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

    return userRepository.save(user);
  }

  @Transactional
  public void deleteUserByEmail(String email) {
    userRepository.deleteByEmail(email);
  }

  @Override
  public List<UsersDto> getAllTeachers() {
    return userRepository
        .findAllByRole(Role.PROFESSOR)
        .orElseThrow(() -> new IllegalArgumentException("No teachers found"))
        .stream()
        .map(
            user ->
                UsersDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .created_at(user.getCreated_at())
                    .updated_at(user.getUpdated_at())
                    .build())
        .toList();
  }

  @Override
  @Transactional
  public void delete(String email) {
    userRepository.deleteByEmail(email);
  }

  @Override
  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      String email = ((UserDetails) principal).getUsername();
      return userRepository
          .findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    throw new RuntimeException("Unauthenticated user");
  }

  private User getUserEntityByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}
