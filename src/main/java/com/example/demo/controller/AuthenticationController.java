package com.example.demo.controller;

import com.example.demo.config.AuthenticationService;
import com.example.demo.config.JwtService;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.LoginUserDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterDTO registerDTO){
        return new ResponseEntity<>(authenticationService.signUp(registerDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        User user = authenticationService.authenticate(loginUserDTO);
        String jwtToken = jwtService.generateToken(new HashMap<>(),user);
        return new ResponseEntity<>(new LoginResponseDTO(jwtToken, jwtService.extractExpiration(jwtToken)), HttpStatus.OK);

    }

}
