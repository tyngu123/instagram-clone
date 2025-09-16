package br.com.yago.socialmedia_api.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.yago.socialmedia_api.dto.JwtAuthenticationResponse;
import br.com.yago.socialmedia_api.dto.LoginRequest;
import br.com.yago.socialmedia_api.dto.SignUpRequest;
import br.com.yago.socialmedia_api.model.Role;
import br.com.yago.socialmedia_api.model.RoleName;
import br.com.yago.socialmedia_api.model.User;
import br.com.yago.socialmedia_api.repository.RoleRepository;
import br.com.yago.socialmedia_api.repository.UserRepository;
import br.com.yago.socialmedia_api.security.JwtTokenProvider;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		// ... (código existente do registerUser)
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>("Email Address already in use!", HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setFullName(signUpRequest.getFullName());
		user.setUsername(signUpRequest.getUsername());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: User Role not found."));
		user.setRoles(Collections.singleton(userRole));

		userRepository.save(user);

		return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
	}

	// --- NOVO MÉTODO ADICIONADO ABAIXO ---
	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		// No JWT, o logout principal é feito no lado do cliente.
		// Este endpoint serve para formalizar a ação e invalidar cookies se estivessem
		// sendo usados.
		// Como estamos usando Authorization Header, apenas retornar OK é suficiente.
		return ResponseEntity.ok("User signed out successfully");
	}
}