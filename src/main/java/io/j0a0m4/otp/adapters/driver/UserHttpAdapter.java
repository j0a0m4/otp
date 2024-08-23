package io.j0a0m4.otp.adapters.driver;

import io.j0a0m4.otp.adapters.driver.requests.CreateUserRequest;
import io.j0a0m4.otp.adapters.driver.response.UserCreatedResponse;
import io.j0a0m4.otp.domain.ports.driver.UserUseCases;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/users")
public final class UserHttpAdapter {

    private final UserUseCases useCases;

    public UserHttpAdapter(final UserUseCases useCases) {
        this.useCases = useCases;
    }

    @PostMapping
    public ResponseEntity<UserCreatedResponse> createUser(
            @RequestBody final CreateUserRequest request,
            final UriComponentsBuilder uriBuilder
    ) {
        final var userId = useCases.createUser(request.email());

        final var location = uriBuilder
                .path("/v1/users/{userId}")
                .build(userId);

        return ResponseEntity
                .created(location)
                .body(new UserCreatedResponse(userId));
    }

}
