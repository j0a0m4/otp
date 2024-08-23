package io.j0a0m4.otp.domain.services;

import io.j0a0m4.otp.domain.exceptions.UnprocessableValueException;

import java.util.Objects;
import java.util.regex.Pattern;

import static io.j0a0m4.otp.domain.services.Validations.Validators.isNonNull;
import static io.j0a0m4.otp.domain.services.Validations.Validators.matchesPattern;


public final class Validations {

    public static <T> void assertNonNull(String property, T value) {
        if (isNonNull().isNotValid(value)) {
            throw new UnprocessableValueException(property, value);
        }
    }

    public static void assertMatchesPattern(String property, String value, Pattern pattern) {
        if (matchesPattern(pattern).isNotValid(value)) {
            throw new UnprocessableValueException(property, value);
        }
    }

    public interface Patterns {

        Pattern EMAIL = Pattern.compile("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+");

    }

    interface Validators {

        static <T> Validator<T> isNonNull() {
            return obj -> !Objects.isNull(obj);
        }

        static Validator<String> matchesPattern(Pattern pattern) {
            return str -> pattern.matcher(str).matches();
        }

    }

}
