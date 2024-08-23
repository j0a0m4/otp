package io.j0a0m4.otp.adapters.driven;

import io.j0a0m4.otp.TestcontainersConfiguration;
import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("Otp Mongo Adapter")
class OtpMongoAdapterTest {

    @Autowired
    private OtpMongoAdapter otpMongoAdapter;

    @Autowired
    private UserMongoRepository userMongoRepository;

    private UUID userId = UUID.randomUUID();

    private Otp otp = Otp.generate();

    @BeforeEach
    void refreshParameters() {
        userId = UUID.randomUUID();
        otp = Otp.generate();
    }

    @Test
    @DisplayName("Should save otp when userId is already created")
    void shouldSaveWhenUserIsPresent() {
        userMongoRepository.save(new User(userId, ""));

        final var actual = otpMongoAdapter.saveIfPresent(userId, otp);

        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(otp);
    }

    @Test
    @DisplayName("Should not save otp when userId was not created before")
    void shouldNotSaveWhenUserIsEmpty() {
        final var actual = otpMongoAdapter.saveIfPresent(userId, otp);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Should not find otp by userId when it wasn't saved before")
    void shouldNotFindOtpByUserId() {
        final var actual = otpMongoAdapter.findBy(userId, otp.otp());
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Should find otp by userId when it was already saved")
    void shouldFindByUserIdAndOtpWhenItsPresent() {
        userMongoRepository.save(new User(userId, ""));
        otpMongoAdapter.saveIfPresent(userId, otp);

        final var actual = otpMongoAdapter.findBy(userId, otp.otp());

        assertThat(actual).isPresent();
        assertThat(actual.get().otp()).isEqualTo(otp.otp());
    }

}