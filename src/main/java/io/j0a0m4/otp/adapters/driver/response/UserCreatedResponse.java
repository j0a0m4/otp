package io.j0a0m4.otp.adapters.driver.response;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record UserCreatedResponse(
        @NonNull UUID userId
) {

}
