package io.j0a0m4.otp.adapters.driver;

import io.j0a0m4.otp.adapters.driver.requests.VerifyOtpRequest;
import io.j0a0m4.otp.config.MapperUtils;
import io.j0a0m4.otp.domain.exceptions.UnprocessableUserException;
import io.j0a0m4.otp.domain.model.OtpStatus;
import io.j0a0m4.otp.domain.ports.driver.OtpUseCases;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("POST /v1/verifications")
public class VerifyOtpStatusHttpAdapterTest {

    private final static String ENDPOINT = "/v1/verifications";

    private String otp = RandomStringUtils.randomNumeric(6);

    private UUID userId = UUID.randomUUID();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OtpHttpAdapter controller;

    @MockBean
    private OtpUseCases useCases;

    @BeforeEach
    void refreshParameters() {
        otp = RandomStringUtils.randomNumeric(6);
        userId = UUID.randomUUID();
    }

    @TestFactory
    @DisplayName("SUCCESS: returns expected otp status")
    public Stream<DynamicTest> shouldReturn2xxWithOtpStatus() {
        return Arrays.stream(OtpStatus.values())
                     .map(this::toSuccesfulDynamicTest);
    }

    private DynamicTest toSuccesfulDynamicTest(OtpStatus status) {
        final var body = new VerifyOtpRequest(userId, otp);

        final var request = post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(MapperUtils.serialize(body));

        return dynamicTest("should return '%s' status".formatted(status), () -> {
            doReturn(status)
                    .when(useCases)
                    .verifyStatusOf(body.userId(), otp);

            mvc.perform(request)
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON))
               .andExpect(jsonPath("$.timestamp").exists())
               .andExpect(jsonPath("$.userId").value(body.userId().toString()))
               .andExpect(jsonPath("$.otp").value(otp))
               .andExpect(jsonPath("$.otpStatus").value(status.toString()));
        });
    }

    @Test
    @DisplayName("FAILURE: should return problem details when userId is not processable")
    public void shouldReturn4xxWithProblemDetailsWhenUserIdIsNotFound() throws Exception {
        final var body = new VerifyOtpRequest(userId, otp);

        final var request = post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(MapperUtils.serialize(body));

        doThrow(new UnprocessableUserException(body.userId()))
                .when(useCases)
                .verifyStatusOf(any(), any());

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isUnprocessableEntity())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.otp").doesNotHaveJsonPath())
           .andExpect(jsonPath("$.type").value("/v1/exceptions/invalid-command"))
           .andExpect(jsonPath("$.title").value("Error validating command"))
           .andExpect(
                   jsonPath("$.detail")
                           .value("User ID by [%s] is not processable".formatted(userId))
           ).andExpect(jsonPath("$.status").value("422"))
           .andExpect(jsonPath("$.metadata").isMap())
           .andExpect(jsonPath("$.metadata.userId").value(userId.toString()));
    }

    @Test
    @DisplayName("FAILURE: should return problem details when no request body is provided")
    public void shouldReturn4xxWhenNoRequestBodyIsProvided() throws Exception {
        final var request = post(ENDPOINT).contentType(APPLICATION_JSON);

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.type").value("/v1/exceptions/unreadable-request-body"))
           .andExpect(jsonPath("$.title").value("Request body is not readable"))
           .andExpect(
                   jsonPath("$.detail")
                           .value("Request body is missing or syntax is wrong")
           ).andExpect(jsonPath("$.status").value("400"))
           .andExpect(jsonPath("$.metadata").doesNotHaveJsonPath());
    }

}