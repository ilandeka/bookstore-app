package com.royaleleague.repository;

import org.springframework.data.repository.CrudRepository;

import com.royaleleague.domain.User;

// Should use JpaRepository<x,y> instead of CrudRepository<x,y> for Spring Boot v2.0+
public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
	
	User findByEmail(String email);
}
