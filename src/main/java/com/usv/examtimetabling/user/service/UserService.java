package com.usv.examtimetabling.user.service;

import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.dto.UsersDto;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

  UserDetailsService userDetailsService();

  List<UsersDto> getAll();

  User getUserByEmail(String email);

  void deleteUserByEmail(String email);

  List<UsersDto> getAllTeachers();

  void delete(String email);

  User getCurrentUser();
}
