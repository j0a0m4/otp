package io.j0a0m4.otp.domain.ports.driver;

import java.util.UUID;

public interface UserUseCases {

    UUID createUser(String email);

}
