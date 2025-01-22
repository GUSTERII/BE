package com.usv.examtimetabling.user.controller;

import com.usv.examtimetabling.materie.model.dto.MaterieDto;
import com.usv.examtimetabling.security.dto.ChangePasswordDto;
import com.usv.examtimetabling.security.services.AuthenticationService;
import com.usv.examtimetabling.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
      "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
      "http://localhost:3000"
    },
    allowCredentials = "true")
public class UserController {

  private final UserService userService;

  private final AuthenticationService authenticationService;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping("/{email}")
  public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserByEmail(email));
  }

  @DeleteMapping("/{email}")
  public void deleteUserByEmail(@PathVariable String email) {
    userService.deleteUserByEmail(email);
  }

  @PreAuthorize("hasAnyAuthority('PROFFESOR', 'ADMIN','STUDENT','GROUP_LEADER')")
  @PostMapping("/change-password")
  public void changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
    authenticationService.changePassword(changePasswordDto);
  }

  @PreAuthorize("hasAnyAuthority('PROFFESOR', 'ADMIN','STUDENT','GROUP_LEADER')")
  @GetMapping("/teachers")
  public ResponseEntity<?> getAllTeachers() {
    return ResponseEntity.ok(userService.getAllTeachers());
  }
 
  @PreAuthorize("hasAnyAuthority('PROFFESOR', 'ADMIN','STUDENT','GROUP_LEADER')")
  @DeleteMapping("/delete")
  public void deleteTeacher(@RequestParam String email) {
    userService.delete(email);
  }
}
