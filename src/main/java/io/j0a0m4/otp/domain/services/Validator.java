package io.j0a0m4.otp.domain.services;

public interface Validator<T> {

    default boolean isNotValid(T t) {
        return !isValid(t);
    }

    boolean isValid(T t);

}
