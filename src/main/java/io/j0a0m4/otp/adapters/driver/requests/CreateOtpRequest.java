package io.j0a0m4.otp.adapters.driver.requests;


import org.springframework.lang.NonNull;

import java.util.UUID;

public record CreateOtpRequest(
        @NonNull UUID userId
) {

}
