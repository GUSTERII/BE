package com.usv.examtimetabling.user.controller;

import com.usv.examtimetabling.security.dto.ChangePasswordDto;
import com.usv.examtimetabling.security.services.AuthenticationService;
import com.usv.examtimetabling.user.model.dto.UpdateUserDto;
import com.usv.examtimetabling.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final AuthenticationService authenticationService;

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(userService.getAll());
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/{email}")
  public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @DeleteMapping("/{email}")
  public void deleteUserByEmail(@PathVariable String email) {
    userService.deleteUserByEmail(email);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PreAuthorize("hasAnyAuthority('PROFFESOR', 'ADMIN','STUDENT','GROUP_LEADER')")
  @PostMapping("/change-password")
  public void changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
    authenticationService.changePassword(changePasswordDto);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PreAuthorize("hasAnyAuthority('PROFFESOR', 'ADMIN','STUDENT','GROUP_LEADER')")
  @GetMapping("/teachers")
  public ResponseEntity<?> getAllTeachers() {
    return ResponseEntity.ok(userService.getAllTeachers());
  }
  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PreAuthorize("hasAnyAuthority('PROFFESOR', 'ADMIN','STUDENT','GROUP_LEADER')")
  @DeleteMapping("/delete")
  public void deleteTeacher(@RequestParam String email) {
    userService.delete(email);
  }
}
