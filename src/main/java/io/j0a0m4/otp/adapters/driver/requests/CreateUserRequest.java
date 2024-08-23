package io.j0a0m4.otp.adapters.driver.requests;


import org.springframework.lang.NonNull;

public record CreateUserRequest(@NonNull String email) {

}
