package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.model.OtpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("OtpStatus Mapper")
class OtpStatusMapperTest {

    @TestFactory
    @DisplayName("Should map otp expiration to OtpStatus")
    Stream<DynamicTest> shouldMapOtpExpirationToOtpStatusSuccessfully() {
        return OtpMockData.TEST_CASES
                .entrySet()
                .stream()
                .map(this::toDynamicTest);
    }

    private DynamicTest toDynamicTest(Map.Entry<OtpStatus, Otp> entry) {
        final var otp = entry.getValue();
        final var expectedStatus = entry.getKey();
        final var otpTime = LocalDateTime.ofInstant(otp.creationTime(), ZoneId.systemDefault());

        final var displayMessage = "Otp created @ %s should map to status %s";
        final var displayTime = "%s:%s".formatted(otpTime.getMinute(), otpTime.getSecond());

        return dynamicTest(displayMessage.formatted(displayTime, expectedStatus), () -> {
            final var actualStatus = new OtpStatusMapper().apply(otp);
            assertThat(actualStatus).isEqualTo(expectedStatus);
        });
    }

}