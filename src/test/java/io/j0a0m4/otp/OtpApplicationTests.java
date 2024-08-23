package io.j0a0m4.otp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DisplayName("Otp Application")
class OtpApplicationTests {

    @Test
    @DisplayName("Should load Spring context")
    void contextLoads() {
    }

}
