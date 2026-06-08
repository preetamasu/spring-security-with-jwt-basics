package com.example.demo.config;


import com.example.demo.dto.LoginUserDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.user.Role;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public User signUp(RegisterDTO input){
        User user = new User();
        user.setEmail(input.username());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setFirstName(input.firstName());
        user.setLastName(input.lastName());
        user.setEnabled(true);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDTO userDTO){
        User user = userRepository.findByEmail(userDTO.email()).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if(!user.isEnabled()){
            throw new RuntimeException("User is not verified. Please go and verify and come back");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.email(),
                        userDTO.password()
                )
        );
        return user;
    }


}
