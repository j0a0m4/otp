package io.j0a0m4.otp.domain.ports.driven;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpStatus;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository {

    Optional<Otp> findBy(UUID userId, String otp);

    Optional<Otp> saveIfPresent(UUID userId, Otp otp);

    Optional<Otp> updateStatusWith(UUID userId, Otp otp, OtpStatus status);

}
