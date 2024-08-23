package io.j0a0m4.otp.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "passwords")
public record OtpRecord(
        @NonNull @Id UUID otpId,
        @NonNull UUID userId,
        @NonNull String otp,
        @NonNull OtpExpiration expiration,
        @NonNull Instant creationTime,
        @NonNull OtpStatus status
) {

    public OtpRecord(UUID userId, Otp otp) {
        this(UUID.randomUUID(), userId, otp.otp(), otp.expiration(), otp.creationTime(), otp.status());
    }

    public Otp toEntity() {
        return new Otp(otp, expiration, creationTime, status);
    }

    public OtpRecord with(OtpStatus updatedStatus) {
        return new OtpRecord(otpId, userId, otp, expiration, creationTime, updatedStatus);
    }

}
