package com.blobstore.nox.auth_module.services;

import com.blobstore.nox.auth_module.models.User;
import com.blobstore.nox.auth_module.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthHelperServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAuthHelperService userAuthHelperService;

    @Test
    void doesUserExistByUsername_returnsTrueWhenPresent() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(new User()));

        assertThat(userAuthHelperService.doesUserExistByUsername("alice")).isTrue();
    }

    @Test
    void doesUserExistByUsername_returnsFalseWhenAbsent() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        assertThat(userAuthHelperService.doesUserExistByUsername("alice")).isFalse();
    }

    @Test
    void getUserByUsername_returnsNullWhenAbsent() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThat(userAuthHelperService.getUserByUsername("missing")).isNull();
    }

    @Test
    void saveUserEntity_delegatesToRepository() {
        User user = User.builder().userId("u1").username("alice").build();
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userAuthHelperService.saveUserEntity(user)).isSameAs(user);
        verify(userRepository).save(user);
    }

    @Test
    void doPasswordsMatch_delegatesToEncoder() {
        when(passwordEncoder.matches("plain", "hash")).thenReturn(true);

        assertThat(userAuthHelperService.doPasswordsMatch("plain", "hash")).isTrue();
    }

    @Test
    void getHashedPassword_delegatesToEncoder() {
        when(passwordEncoder.encode("secret")).thenReturn("hashed");

        assertThat(userAuthHelperService.getHashedPassword("secret")).isEqualTo("hashed");
    }
}
