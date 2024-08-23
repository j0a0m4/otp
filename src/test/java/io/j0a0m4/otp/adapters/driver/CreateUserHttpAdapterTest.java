package io.j0a0m4.otp.adapters.driver;

import io.j0a0m4.otp.adapters.driver.requests.CreateUserRequest;
import io.j0a0m4.otp.domain.exceptions.UnprocessableValueException;
import io.j0a0m4.otp.domain.ports.driver.UserUseCases;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;
import java.util.stream.Stream;

import static io.j0a0m4.otp.config.MapperUtils.serialize;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("POST /v1/users")
class CreateUserHttpAdapterTest {

    private final static String ENDPOINT = "/v1/users";

    private UUID userId = UUID.randomUUID();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserHttpAdapter userHttpAdapter;

    @MockBean
    private UserUseCases useCases;

    @BeforeEach
    void refreshParameters() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("SUCCESS: returns new user id in body and location header")
    void shouldReturn2xxWithNewUserId() throws Exception {
        final var email = RandomStringUtils.randomAlphabetic(12) + "@test.com";

        final var request = post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(serialize(new CreateUserRequest(email)));

        doReturn(userId).when(useCases).createUser(email);

        mvc.perform(request)
           .andDo(print())
           .andExpect(status().isCreated())
           .andExpect(content().contentType(APPLICATION_JSON))
           .andExpect(jsonPath("$.userId").value(userId.toString()))
           .andExpect(header().string("Location", containsString(userId.toString())));

        verify(useCases, times(1)).createUser(email);
    }


    @TestFactory
    @DisplayName("FAILURE: should return problem details when request body email is unreadable")
    Stream<DynamicTest> shouldReturn4xxWhenRequestBodyIsInvalid() {
        return Stream.of("", " ", null, "asdsdasd", "@", ".com", "test.com", "a@test", "@test.com", " @test.com")
                     .map(this::buildUnreadableEmailCase);
    }

    @SuppressWarnings("UnnecessaryCallToStringValueOf")
    public DynamicTest buildUnreadableEmailCase(String email) {
        final var request = post(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .content(serialize(new CreateUserRequest(email)));

        return dynamicTest("'%s' is not a valid email".formatted(email), () -> {
            final var emailValue = String.valueOf(email);

            doThrow(new UnprocessableValueException("email", email))
                    .when(useCases)
                    .createUser(email);

            mvc.perform(request)
               .andDo(print())
               .andExpect(status().isUnprocessableEntity())
               .andExpect(content().contentType(APPLICATION_JSON))
               .andExpect(jsonPath("$.otp").doesNotHaveJsonPath())
               .andExpect(jsonPath("$.type").value("/v1/exceptions/invalid-command"))
               .andExpect(jsonPath("$.title").value("Error validating command"))
               .andExpect(jsonPath("$.detail").value(
                       "Value of '%s' for property '%s' is not processable".formatted(email, "email")))
               .andExpect(jsonPath("$.status").value("422"))
               .andExpect(jsonPath("$.metadata").isMap())
               .andExpect(jsonPath("$.metadata.email").value(emailValue));
        });
    }


    @Test
    @DisplayName("FAILURE: should return problem details when no request body is provided")
    public void shouldReturn4xxWhenNoRequestBodyIsProvided() throws Exception {
        mvc.perform(post(ENDPOINT))
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