package com.journalintime.service;

import com.journalintime.model.User;
import com.journalintime.repository.UserRepository;
import com.journalintime.persistence.hibernate.UserHibernateRepository;
import com.journalintime.dto.UserDTO;
import com.journalintime.mapper.Mapper;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserHibernateRepository();
    }

    public UserDTO register(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        user = userRepository.save(user);
        return Mapper.toDTO(user);
    }

    public UserDTO login(String username, String password) {
        System.out.println("Attempting login for: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found. Comparing passwords...");
            if (user.getPassword().equals(password)) {
                System.out.println("Login successful.");
                return Mapper.toDTO(user);
            } else {
                System.out.println("Password mismatch.");
            }
        } else {
            System.out.println("User not found.");
        }
        throw new RuntimeException("Invalid credentials");
    }

    public UserDTO updateUserPreferences(UserDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(dto.getEmail());
        user.setThemeMode(dto.getThemeMode());
        user.setLanguage(dto.getLanguage());
        user.setPreferredFont(dto.getPreferredFont());
        user.setAvatarPath(dto.getAvatarPath());

        user = userRepository.update(user);
        return Mapper.toDTO(user);
    }
}
