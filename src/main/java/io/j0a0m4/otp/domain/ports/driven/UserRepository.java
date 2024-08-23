package io.j0a0m4.otp.domain.ports.driven;

import io.j0a0m4.otp.domain.model.User;

public interface UserRepository {

    User save(User user);

}
