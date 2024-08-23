package io.j0a0m4.otp.domain.model;

import io.j0a0m4.otp.domain.services.OtpStatusMapper;

public enum OtpStatus {
    PENDING,
    ACCEPTED,
    EXPIRED,
    ;

    public static OtpStatus from(Otp otp) {
        return new OtpStatusMapper().apply(otp);
    }
}
