package com.task.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.user.config.JwtProvider;
import com.task.user.model.User;
import com.task.user.repository.UserRepository;
import com.task.user.request.LoginRequest;
import com.task.user.response.AuthResponse;
import com.task.user.service.CustomerUserServiceImplementation;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerUserServiceImplementation customerUserService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
            CustomerUserServiceImplementation customerUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerUserService = customerUserService;
    }

    @CrossOrigin(origins = "http://localhost:5173/")
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();

        User IsEmailExist = userRepository.findByEmail(email);

        if (IsEmailExist != null) {
            throw new Exception("Email already exists");
        }

        User createUser = new User();
        createUser.setName(name);
        createUser.setEmail(email);
        createUser.setPassword(passwordEncoder.encode(password));
        createUser.setRole(role);

        User savedUser = userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("User created successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173/")
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setToken(token);
        authResponse.setMessage("User logged in successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customerUserService.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // @PostMapping("/logout")
    // public ResponseEntity<?> logout(HttpServletRequest request) {
    //     String token = getTokenFromRequest(request);

    //     // Adicione o token a uma lista negra ou revogue o token aqui

    //     return ResponseEntity.ok(new MessageResponse("Logged out successfully!"));
    // }

    // private String getTokenFromRequest(HttpServletRequest request) {
    //     String bearerToken = request.getHeader("Authorization");
    //     if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
    //         return bearerToken.substring(7);
    //     }
    //     return null;
    // }
}
