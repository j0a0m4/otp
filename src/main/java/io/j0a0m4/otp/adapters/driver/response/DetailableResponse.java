package io.j0a0m4.otp.adapters.driver.response;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

public record DetailableResponse(
        @NonNull int status,
        @NonNull String type,
        @NonNull String title,
        @NonNull String detail,
        @Nullable Map<String, ?> metadata
) {

}
