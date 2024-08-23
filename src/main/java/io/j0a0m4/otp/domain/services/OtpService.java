package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.exceptions.UnprocessableUserException;
import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpStatus;
import io.j0a0m4.otp.domain.ports.driven.OtpRepository;
import io.j0a0m4.otp.domain.ports.driver.OtpUseCases;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public final class OtpService implements OtpUseCases {

    private final OtpRepository otpRepository;

    public OtpService(final OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Override
    public Otp createOtpFor(final UUID userId) {
        Validations.assertNonNull("userId", userId);

        final var otp = Otp.generate();

        return otpRepository
                .saveIfPresent(userId, otp)
                .orElseThrow(() -> new UnprocessableUserException(userId));
    }

    @Override
    public OtpStatus verifyStatusOf(final UUID userId, final String otp) {
        final Otp otpEntity = otpRepository
                .findBy(userId, otp)
                .orElseThrow(() -> new UnprocessableUserException(userId));

        if (Objects.equals(OtpStatus.EXPIRED, otpEntity.status())) {
            return OtpStatus.EXPIRED;
        }

        final var status = OtpStatus.from(otpEntity);

        if (!Objects.equals(otpEntity.status(), status)) {
            otpRepository.updateStatusWith(userId, otpEntity, status);
        }

        return status;
    }

}
