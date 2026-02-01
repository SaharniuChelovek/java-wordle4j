package ru.yandex.practicum;

public class EmptyLineException extends Exception {
    public EmptyLineException() {
        super();
    }

    public EmptyLineException(String message) {
        super(message);
    }
}
