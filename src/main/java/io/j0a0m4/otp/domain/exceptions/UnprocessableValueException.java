package io.j0a0m4.otp.domain.exceptions;

import java.util.Map;

public class UnprocessableValueException extends UnprocessableCommandException {

    public UnprocessableValueException(String property, Object value) {
        super(("Value of '%s' for property '%s' is not processable").formatted(value, property),
              Map.of(property, String.valueOf(value)));
    }

}
