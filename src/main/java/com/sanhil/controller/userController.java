package com.sanhil.controller;

import com.sanhil.repository.userRepository;
import com.sanhil.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class userController {
	@Autowired
	private userRepository UserRepository;

	@PostMapping(value = "/signup")
	private ResponseEntity<?> addUser(@RequestBody userService user){
		if(user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null){
			return ResponseEntity.badRequest().body("Please provide all fields");
		}
		userService existingUser = UserRepository.findByEmail(user.getEmail());
		if(existingUser  != null){
			return ResponseEntity.badRequest().body("Email is already Exist");
		}
		user.setFirstName(user.getFirstName().toLowerCase().trim());
		user.setLastName(user.getLastName().toLowerCase().trim());

		//hash password
		String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(hashedPassword);

		userService savedUser = UserRepository.save(user);
		savedUser.setPassword("मैं नहीं बताऊँगा");
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	@PostMapping(value = "/login")
	private ResponseEntity<?> loginUser(@RequestBody userService loginUser){
		if(loginUser.getEmail() == null || loginUser.getPassword() == null){
			return ResponseEntity.badRequest().body("Please provide email or password");
		}
		userService existingUser = UserRepository.findByEmail(loginUser.getEmail());
		if(existingUser == null){
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please provide valid email");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(!encoder.matches(loginUser.getPassword(),existingUser.getPassword())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
		}
		existingUser.setPassword("मैं नहीं बताऊँगा!");
		return  ResponseEntity.status(HttpStatus.OK).body(existingUser);
	}

	@GetMapping(value = "/allUser")
	private List<userService> getAllUser(){
		return UserRepository.findAll();
	}
}