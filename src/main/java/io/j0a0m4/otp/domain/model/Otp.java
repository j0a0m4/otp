package io.j0a0m4.otp.domain.model;

import org.springframework.lang.NonNull;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

public record Otp(
        @NonNull String otp,
        @NonNull OtpExpiration expiration,
        @NonNull Instant creationTime,
        @NonNull OtpStatus status
) {

    private Otp(@NonNull String otp) {
        this(otp, new OtpExpiration(Duration.ofSeconds(30)), Instant.now(), OtpStatus.PENDING);
    }

    // Warning! This does not follow the Open Auth Initiative for OTP.
    // It's meant as an exercise for modeling only!
    // For production, you must use only HOTP and TOTP algorithms:
    // https://www.rfc-editor.org/rfc/rfc4226
    // https://www.rfc-editor.org/rfc/rfc6238
    public static Otp generate() {
        final var decimalFormat = new DecimalFormat("000000");
        final var rand = new SecureRandom();
        final var value = rand.nextInt(999_999);
        return new Otp(decimalFormat.format(value));
    }

    public Otp withStatus(OtpStatus status) {
        return new Otp(otp, expiration, creationTime, status);
    }

}
