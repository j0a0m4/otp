package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpExpiration;
import io.j0a0m4.otp.domain.model.OtpStatus;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

interface OtpMockData {

    Otp EXPIRED_OTP = new Otp(
            RandomStringUtils.randomNumeric(6),
            new OtpExpiration(Duration.ofSeconds(15)),
            Instant.now().minusSeconds(16),
            OtpStatus.EXPIRED
    );

    Otp VALID_OTP = new Otp(
            RandomStringUtils.randomNumeric(6),
            new OtpExpiration(Duration.ofSeconds(15)),
            Instant.now(),
            OtpStatus.PENDING
    );

    // Key: Expected OtpStatus
    // Value: Otp input to be processed
    Map<OtpStatus, Otp> TEST_CASES = Map.of(
            OtpStatus.EXPIRED, EXPIRED_OTP,
            OtpStatus.ACCEPTED, VALID_OTP
    );

}
