package io.j0a0m4.otp.adapters.driver;

import io.j0a0m4.otp.adapters.driver.requests.CreateOtpRequest;
import io.j0a0m4.otp.config.MapperUtils;
import io.j0a0m4.otp.domain.exceptions.UnprocessableUserException;
import io.j0a0m4.otp.domain.exceptions.UnprocessableValueException;
import io.j0a0m4.otp.domain.model.Otp;
import io.j0a0m4.otp.domain.ports.driver.OtpUseCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("POST /v1/otp")
class CreateOtpHttpAdapterTest {

    private static final String ENDPOINT = "/v1/otp";

    private UUID userId = UUID.randomUUID();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OtpHttpAdapter controller;

    @MockBean
    private OtpUseCases useCases;

    @BeforeEach
    void refreshParameters() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("SUCCESS: should return otp when userId is valid")
    public void shouldReturn2xxWithOtpWhenUserIdIsValid() throws Exception {
        final var otp = Otp.generate();
        final var body = new CreateOtpRequest(userId);

        final var request = post(ENDPOINT).contentType(APPLICATION_JSON).content(MapperUtils.serialize(body));

        doReturn(otp).when(useCases).createOtpFor(body.userId());

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isAccepted())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.otp").value(otp.otp()))
           .andExpect(jsonPath("$.userId").value(body.userId().toString()))
           .andExpect(jsonPath("$.expiration.expiresIn").value(otp.expiration().expiresIn()))
           .andExpect(jsonPath("$.expiration.measurementUnit").value(SECONDS.toString()))
           .andExpect(jsonPath("$.creationTime").value(otp.creationTime().toString()));
    }

    @Test
    @DisplayName("FAILURE: should return problem details when userId is not processable")
    public void shouldReturn4xxWithProblemDetailsWhenUserIdIsNotFound() throws Exception {
        final var body = new CreateOtpRequest(userId);

        final var request = post(ENDPOINT).contentType(APPLICATION_JSON).content(MapperUtils.serialize(body));

        doThrow(new UnprocessableUserException(body.userId())).when(useCases).createOtpFor(any());

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isUnprocessableEntity())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.otp").doesNotHaveJsonPath())
           .andExpect(jsonPath("$.type").value("/v1/exceptions/invalid-command"))
           .andExpect(jsonPath("$.title").value("Error validating command"))
           .andExpect(jsonPath("$.detail").value("User ID by [%s] is not processable".formatted(userId)))
           .andExpect(jsonPath("$.status").value("422"))
           .andExpect(jsonPath("$.metadata").isMap())
           .andExpect(jsonPath("$.metadata.userId").value(userId.toString()));
    }

    @Test
    @SuppressWarnings({"ConstantValue", "DataFlowIssue"})
    @DisplayName("FAILURE: should return problem details when request body has null props")
    public void shouldReturn4xxWithProblemDetailsWhenUserIdIsNull() throws Exception {
        final UUID userId = null;
        final var body = new CreateOtpRequest(userId);

        final var request = post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(MapperUtils.serialize(body));

        doThrow(new UnprocessableValueException("userId", userId))
                .when(useCases)
                .createOtpFor(userId);

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isUnprocessableEntity())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.otp").doesNotHaveJsonPath())
           .andExpect(jsonPath("$.type").value("/v1/exceptions/invalid-command"))
           .andExpect(jsonPath("$.title").value("Error validating command"))
           .andExpect(jsonPath("$.detail").value(
                   "Value of '%s' for property '%s' is not processable".formatted(userId, "userId")))
           .andExpect(jsonPath("$.status").value("422"))
           .andExpect(jsonPath("$.metadata").isMap())
           .andExpect(jsonPath("$.metadata.userId").value(String.valueOf(userId)));
    }

    @Test
    @DisplayName("FAILURE: should return problem details when no request body is provided")
    public void shouldReturn4xxWhenUserIdIsInvalid() throws Exception {
        final var request = post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(MapperUtils.serialize(Map.of("userId", "q8ywr8qwyrqw3")));

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.type").value("/v1/exceptions/unreadable-request-body"))
           .andExpect(jsonPath("$.title").value("Request body is not readable"))
           .andExpect(jsonPath("$.detail").value("Request body is missing or syntax is wrong"))
           .andExpect(jsonPath("$.status").value("400"))
           .andExpect(jsonPath("$.metadata").doesNotHaveJsonPath());
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
           .andExpect(jsonPath("$.detail").value("Request body is missing or syntax is wrong"))
           .andExpect(jsonPath("$.status").value("400"))
           .andExpect(jsonPath("$.metadata").doesNotHaveJsonPath());
    }

}
