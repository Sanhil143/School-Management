package com.sanhil.controller;

import com.sanhil.model.jwtRequest;
import com.sanhil.model.jwtResponse;
import com.sanhil.repository.userRepository;
import com.sanhil.security.jwtHelper;
import com.sanhil.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class userController {
	@Autowired
	private userRepository UserRepository;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private jwtHelper helper;


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
	private ResponseEntity<jwtResponse> loginUser(@RequestBody jwtRequest request){
		if(request.getEmail() == null || request.getPassword() == null){
			return ResponseEntity.badRequest().body(new jwtResponse("Please provide email or password", null));
		}
		userService existingUser = UserRepository.findByEmail(request.getEmail());
		if(existingUser == null){
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new jwtResponse("Invalid email or password", null));
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(!encoder.matches(request.getPassword(),existingUser.getPassword())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new jwtResponse("Invalid email or password", null));
		}
		existingUser.setPassword("मैं नहीं बताऊँगा!");
		jwtResponse response = new jwtResponse("Login successful", existingUser.getId());
		return  ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/allUser")
	private List<userService> getAllUser(){
		return UserRepository.findAll();
	}
}
