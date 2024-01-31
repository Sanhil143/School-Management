package com.sanhil.controller;

import com.sanhil.repository.userRepository;
import com.sanhil.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class userController {
	@Autowired
	private userRepository userrepository;

	@PostMapping(value = "/signup")
	private ResponseEntity<?> addUser(@RequestBody userService user){
		if(user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null){
			return ResponseEntity.badRequest().body("Please provide all fields");
		}
		if(userrepository.existsByEmail(user.getEmail())){
			return ResponseEntity.badRequest().body("Email is already required");
		}
    user.setFirstName(user.getFirstName().toLowerCase().trim());
		user.setLastName(user.getLastName().toLowerCase().trim());

		userService savedUser = userrepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	@GetMapping(value = "/allUser")
	private List<userService> getAllUser(){
		return userrepository.findAll();
	}
}
