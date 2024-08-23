package io.j0a0m4.otp.adapters.driver;

import io.j0a0m4.otp.adapters.driver.response.DetailableResponse;
import io.j0a0m4.otp.domain.exceptions.DetailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public final class HttpErrorHandler {

    @ExceptionHandler(DetailableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public DetailableResponse handleDetailableException(DetailableException exception) {
        return new DetailableResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                exception.getType(),
                exception.getTitle(),
                exception.getDetail(),
                exception.getMetadata()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public DetailableResponse handleMessageNotReadableException() {
        return new DetailableResponse(
                HttpStatus.BAD_REQUEST.value(),
                "/v1/exceptions/unreadable-request-body",
                "Request body is not readable",
                "Request body is missing or syntax is wrong",
                null
        );
    }

}

