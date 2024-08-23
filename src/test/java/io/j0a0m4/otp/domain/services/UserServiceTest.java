package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.exceptions.UnprocessableValueException;
import io.j0a0m4.otp.domain.model.User;
import io.j0a0m4.otp.domain.ports.driven.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("User Service")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("SUCCESS: new user is created successfully")
    void shouldCreateUserSuccessfully() {
        final var user = new User("example@test.com");
        final var captor = ArgumentCaptor.forClass(User.class);

        doReturn(user)
                .when(userRepository)
                .save(any());

        final var userId = userService.createUser(user.email());

        verify(userRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().email()).isEqualTo(user.email());
        assertThat(userId).isNotNull();
    }


    @TestFactory
    @DisplayName("FAILURE: email value is unprocessable")
    Stream<DynamicTest> shouldThrowWhenEmailIsUnprocessable() {
        return Stream.of("", " ", null, "asdsdasd", "@", ".com", "test.com", "a@test", "@test.com", " @test.com")
                     .map(this::buildUnprocessableEmailCase);
    }


    private DynamicTest buildUnprocessableEmailCase(String email) {
        return dynamicTest("'%s' is not processable".formatted(email), () -> {
            assertThatThrownBy(() -> userService.createUser(email))
                    .isInstanceOf(UnprocessableValueException.class)
                    .hasMessageContaining("email");

            verify(userRepository, never()).save(any());
        });
    }

}