package com.royaleleague;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.royaleleague.domain.User;
import com.royaleleague.domain.security.Role;
import com.royaleleague.domain.security.UserRole;
import com.royaleleague.service.UserService;
import com.royaleleague.utility.SecurityUtility;

@SpringBootApplication
public class RoyaleleagueApplication implements CommandLineRunner {

  @Autowired
  private UserService userService;

  public static void main(String[] args) {
    SpringApplication.run(RoyaleleagueApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    User user1 = new User();
    user1.setFirstName("john");
    user1.setLastName("doe");
    user1.setUsername("jd");
    user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
    user1.setEmail("johndoe@gmail.com");
    Set<UserRole> userRoles = new HashSet<>();
    Role role1 = new Role();
    role1.setRoleId(1);
    role1.setName("ROLE_USER");
    userRoles.add(new UserRole(user1, role1));

    userService.createUser(user1, userRoles);
  }
}
