package io.j0a0m4.otp.domain.exceptions;

import org.springframework.lang.NonNull;

import java.util.Map;

public class UnprocessableCommandException extends DetailableException {

    public UnprocessableCommandException(
            @NonNull String detail,
            @NonNull Map<String, ?> metadata
    ) {
        super("/v1/exceptions/invalid-command", "Error validating command", detail, metadata);
    }

}
