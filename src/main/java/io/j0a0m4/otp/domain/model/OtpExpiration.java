package io.j0a0m4.otp.domain.model;

import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public record OtpExpiration(
        @NonNull Long expiresIn,
        @NonNull ChronoUnit measurementUnit
) {

    public OtpExpiration(Duration duration) {
        this(duration.toSeconds(), ChronoUnit.SECONDS);
    }

}
