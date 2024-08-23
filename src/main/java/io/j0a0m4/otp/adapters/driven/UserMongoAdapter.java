package io.j0a0m4.otp.adapters.driven;

import io.j0a0m4.otp.domain.model.User;
import io.j0a0m4.otp.domain.ports.driven.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserMongoAdapter implements UserRepository {

    private final UserMongoRepository userRepository;

    public UserMongoAdapter(final UserMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

}
