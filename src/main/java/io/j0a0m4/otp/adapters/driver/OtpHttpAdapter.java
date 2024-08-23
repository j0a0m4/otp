package io.j0a0m4.otp.adapters.driver;

import io.j0a0m4.otp.adapters.driver.requests.CreateOtpRequest;
import io.j0a0m4.otp.adapters.driver.requests.VerifyOtpRequest;
import io.j0a0m4.otp.adapters.driver.response.OtpCreatedResponse;
import io.j0a0m4.otp.adapters.driver.response.OtpVerificationResponse;
import io.j0a0m4.otp.domain.ports.driver.OtpUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public final class OtpHttpAdapter {

    private final OtpUseCases useCases;

    public OtpHttpAdapter(final OtpUseCases useCases) {
        this.useCases = useCases;
    }

    @PostMapping("/otp")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OtpCreatedResponse createOtp(@RequestBody final CreateOtpRequest request) {
        final var otp = useCases.createOtpFor(request.userId());
        return new OtpCreatedResponse(request.userId(), otp);
    }

    @PostMapping("/verifications")
    @ResponseStatus(HttpStatus.OK)
    public OtpVerificationResponse verifyOtpStatus(@RequestBody final VerifyOtpRequest request) {
        final var status = useCases.verifyStatusOf(request.userId(), request.otp());
        return new OtpVerificationResponse(request.userId(), request.otp(), status);
    }

}
