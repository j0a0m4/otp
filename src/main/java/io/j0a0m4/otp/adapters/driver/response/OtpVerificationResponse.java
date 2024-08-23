package io.j0a0m4.otp.adapters.driver.response;

import io.j0a0m4.otp.domain.model.OtpStatus;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

public record OtpVerificationResponse(
        @NonNull UUID userId,
        @NonNull String otp,
        @NonNull OtpStatus otpStatus,
        @NonNull Instant timestamp
) {

    public OtpVerificationResponse(UUID userId, String otp, OtpStatus otpStatus) {
        this(userId, otp, otpStatus, Instant.now());
    }

}
