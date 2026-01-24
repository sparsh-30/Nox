package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.models.User;
import com.blobstore.nox.auth_module.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthHelperService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Boolean doesUserExistByUsername(String username) {
        Optional<User> u = userRepository.findByUsername(username);
        return u.isPresent();
    }

    public Boolean doesUserExistByUserId(String userId) {
        Optional<User> u = userRepository.findByUserId(userId);
        return u.isPresent();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    public User saveUserEntity(User u) {
        return userRepository.save(u);
    }

    public Boolean doPasswordsMatch(String userProvidedPassword, String savedPassword) {
        return passwordEncoder.matches(userProvidedPassword, savedPassword);
    }

    public String getHashedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
