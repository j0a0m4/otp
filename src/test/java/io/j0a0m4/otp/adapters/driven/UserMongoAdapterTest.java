package io.j0a0m4.otp.adapters.driven;

import io.j0a0m4.otp.TestcontainersConfiguration;
import io.j0a0m4.otp.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("User Mongo Adapter")
class UserMongoAdapterTest {

    @Autowired
    private UserMongoAdapter userMongoAdapter;

    @Autowired
    private UserMongoRepository userRepository;

    @Test
    @DisplayName("SUCCESS: Should save user successfully")
    void shouldSaveUser() {
        final var user = new User(UUID.randomUUID(), "joe@test.com");

        assertThatCode(() -> userMongoAdapter.save(user)).doesNotThrowAnyException();

        final var actual = userRepository.findById(user.id());

        assertThat(actual).isPresent().get().extracting("id").isEqualTo(user.id());
    }

}