package io.j0a0m4.otp.domain.ports.driver;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpStatus;

import java.util.UUID;

public interface OtpUseCases {

    Otp createOtpFor(UUID userId);

    OtpStatus verifyStatusOf(UUID userId, String otp);

}
