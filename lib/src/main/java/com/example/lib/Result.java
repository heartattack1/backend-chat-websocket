package com.example.lib;

import java.util.Objects;

public sealed interface Result<T> permits Result.Success, Result.Failure {

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(Throwable error) {
        return new Failure<>(error);
    }

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }
    T getOrNull();
    Throwable errorOrNull();

    record Success<T>(T value) implements Result<T> {
        public Success {
            Objects.requireNonNull(value, "value");
        }

        @Override public boolean isSuccess() { return true; }

        @Override public T getOrNull() { return value; }

        @Override public Throwable errorOrNull() { return null; }
    }

    record Failure<T>(Throwable error) implements Result<T> {
        public Failure {
            Objects.requireNonNull(error, "error");
        }

        @Override public boolean isSuccess() { return false; }

        @Override public T getOrNull() { return null; }

        @Override public Throwable errorOrNull() { return error; }
    }
}

