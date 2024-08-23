package io.j0a0m4.otp.domain.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.UUID;


@Document(collection = "users")
public record User(
        @NonNull @Id UUID id,
        @NonNull String email
) {

    public User(@NonNull String email) {
        this(UUID.randomUUID(), email);
    }

}
