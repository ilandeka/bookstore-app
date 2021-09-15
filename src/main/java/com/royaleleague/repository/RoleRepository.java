package com.royaleleague.repository;

import org.springframework.data.repository.CrudRepository;

import com.royaleleague.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByName(String name);
}
