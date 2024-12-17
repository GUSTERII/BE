package com.usv.examtimetabling.user.repository;


import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.utils.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  void deleteByEmail(String email);

  Optional<List<User>> findAllByRole(Role role);

  Optional<User> findByName(String name);
}
