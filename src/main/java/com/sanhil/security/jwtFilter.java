package com.sanhil.security;

import com.sanhil.repository.userRepository;
import com.sanhil.service.userService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class jwtFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
	@Autowired
	private jwtHelper jwthelper;


	@Autowired
	private userService userservice;
	private userRepository userrepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
		//Authorization

		String requestHeader = request.getHeader("Authorization");
		//Bearer 2352345235sdfrsfgsdfsdf
		logger.info(" Header :  {}", requestHeader);
		Integer userId = null;
		String token = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			//looking good
			token = requestHeader.substring(7);
			try {

				userId = this.jwthelper.getUseridfromToken(token);

			} catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				logger.info("Given jwt token is expired !!");
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}


		} else {
			logger.info("Invalid Header Value !! ");
		}


		//
		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			Optional<userService> userDetailsOptional = this.userrepository.findById(userId);
			if (userDetailsOptional.isPresent()) {
				UserDetails userDetails = (UserDetails) userDetailsOptional.get();
				Boolean validateToken = this.jwthelper.validateToken(token, userservice);
				if (validateToken) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					logger.error("Token validation fails !!");
				}
			} else {
				logger.error("User details not found for user ID: {}", userId);
			}

		}

		filterChain.doFilter(request, response);


	}
}