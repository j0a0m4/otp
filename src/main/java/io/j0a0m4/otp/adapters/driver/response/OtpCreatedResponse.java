package io.j0a0m4.otp.adapters.driver.response;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpExpiration;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

public record OtpCreatedResponse(
        @NonNull UUID userId,
        @NonNull String otp,
        @NonNull OtpExpiration expiration,
        @NonNull Instant creationTime
) {

    public OtpCreatedResponse(@NonNull UUID userId, @NonNull Otp otp) {
        this(userId, otp.otp(), otp.expiration(), otp.creationTime());
    }

}

