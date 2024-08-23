package io.j0a0m4.otp;

import org.springframework.boot.SpringApplication;

public class TestOtpApplication {

    public static void main(String[] args) {
        SpringApplication.from(OtpApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
