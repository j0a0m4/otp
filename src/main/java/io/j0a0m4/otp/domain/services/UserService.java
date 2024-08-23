package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.model.User;
import io.j0a0m4.otp.domain.ports.driven.UserRepository;
import io.j0a0m4.otp.domain.ports.driver.UserUseCases;
import io.j0a0m4.otp.domain.services.Validations.Patterns;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class UserService implements UserUseCases {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID createUser(final String email) {
        Validations.assertNonNull("email", email);
        Validations.assertMatchesPattern("email", email, Patterns.EMAIL);
        final var user = new User(email);
        return userRepository.save(user).id();
    }

}
