package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.exceptions.UnprocessableUserException;
import io.j0a0m4.otp.domain.exceptions.UnprocessableValueException;
import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpStatus;
import io.j0a0m4.otp.domain.ports.driven.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@SpringBootTest
@DisplayName("OTP Service")
class OtpServiceTest {

    private UUID userId = UUID.randomUUID();

    private Otp otp = Otp.generate();

    @Autowired
    private OtpService otpService;

    @MockBean
    private OtpRepository otpRepository;

    @BeforeEach
    void refreshVariables() {
        userId = UUID.randomUUID();
        otp = Otp.generate();
    }

    @Test
    @DisplayName("SUCCESS: Should create new otp for valid userId")
    void shouldCreateNewOtpSuccessfully() {
        final var uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        final var otpCaptor = ArgumentCaptor.forClass(Otp.class);

        doReturn(Optional.of(otp)).when(otpRepository).saveIfPresent(any(), any());

        final var actual = otpService.createOtpFor(userId);

        verify(otpRepository, times(1)).saveIfPresent(uuidCaptor.capture(), otpCaptor.capture());

        assertThat(actual).hasNoNullFieldsOrProperties()
                          .hasFieldOrProperty("creationTime")
                          .hasFieldOrPropertyWithValue("otp", otp.otp())
                          .extracting("expiration")
                          .hasFieldOrPropertyWithValue("expiresIn", 30L)
                          .hasFieldOrPropertyWithValue("measurementUnit", SECONDS);

        assertThat(uuidCaptor.getValue()).isEqualTo(userId);
        assertThat(otpCaptor.getValue()).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("FAILURE: Should throw when user id not found")
    void shouldThrowWhenUserNotFoundWhenCreatingOtp() {
        doReturn(Optional.empty()).when(otpRepository).saveIfPresent(any(), any());

        assertThatThrownBy(() -> otpService.createOtpFor(userId)).isInstanceOf(UnprocessableUserException.class)
                                                                 .hasMessageContaining(userId.toString());

        verify(otpRepository, times(1)).saveIfPresent(any(), any());
    }

    @Test
    @DisplayName("FAILURE: Should throw when user id is null")
    void shouldThrowWhenUserIdIsNull() {
        final UUID userId = null;

        assertThatThrownBy(() -> otpService.createOtpFor(userId)).isInstanceOf(UnprocessableValueException.class)
                                                                 .hasMessageContaining("userId");

        verify(otpRepository, never()).saveIfPresent(any(), any());
    }

    @Test
    @DisplayName("FAILURE: should throw when user not found")
    void shouldThrowWhenUserNotFoundWhenVerifyingOtp() {
        doReturn(Optional.empty())
                .when(otpRepository)
                .findBy(userId, otp.otp());

        assertThatThrownBy(() -> otpService.verifyStatusOf(userId, otp.otp())).isInstanceOf(
                UnprocessableUserException.class).hasMessageContaining(userId.toString());

        verify(otpRepository, times(1)).findBy(userId, otp.otp());
    }

    @Test
    @DisplayName("SUCCESS: should only return status when it's expired")
    void shouldReturnStatusWhenItsExpired() {
        final var expiredOtp = OtpMockData.EXPIRED_OTP;

        doReturn(Optional.of(expiredOtp))
                .when(otpRepository)
                .findBy(userId, expiredOtp.otp());

        final var actual = otpService.verifyStatusOf(userId, expiredOtp.otp());

        assertThat(actual).isEqualTo(OtpStatus.EXPIRED);

        verify(otpRepository, times(1)).findBy(userId, expiredOtp.otp());
        verify(otpRepository, never()).saveIfPresent(any(), any());
    }

    @Test
    @DisplayName("SUCCESS: should update new status when it changes from pending to accept")
    void shouldMapToNewStatusWhenItsAccepted() {
        final var pendingOtp = OtpMockData.VALID_OTP;

        doReturn(Optional.of(pendingOtp))
                .when(otpRepository)
                .findBy(userId, pendingOtp.otp());

        final var actual = otpService.verifyStatusOf(userId, pendingOtp.otp());

        assertThat(actual).isEqualTo(OtpStatus.ACCEPTED);

        verify(otpRepository, times(1)).findBy(userId, pendingOtp.otp());
        verify(otpRepository, times(1)).updateStatusWith(userId, pendingOtp, OtpStatus.ACCEPTED);
    }

}