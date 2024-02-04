package com.sanhil.repository;

import com.sanhil.service.userService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<userService,Integer> {
	userService findByEmail(String email);
}
