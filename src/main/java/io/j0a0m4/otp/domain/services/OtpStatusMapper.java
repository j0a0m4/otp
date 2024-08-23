package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpStatus;

import java.time.Instant;
import java.util.function.Function;

public final class OtpStatusMapper implements Function<Otp, OtpStatus> {

    @Override
    public OtpStatus apply(final Otp otp) {
        final var timeDiff = otp
                .expiration()
                .measurementUnit()
                .between(otp.creationTime(), Instant.now());

        if (timeDiff < otp.expiration().expiresIn()) {
            return OtpStatus.ACCEPTED;
        }

        return OtpStatus.EXPIRED;
    }

}
