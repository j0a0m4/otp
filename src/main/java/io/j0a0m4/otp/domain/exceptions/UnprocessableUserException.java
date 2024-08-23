package io.j0a0m4.otp.domain.exceptions;

import java.util.Map;
import java.util.UUID;

public class UnprocessableUserException extends UnprocessableCommandException {

    public UnprocessableUserException(UUID userId) {
        super("User ID by [%s] is not processable".formatted(userId), Map.of("userId", userId));
    }

}
