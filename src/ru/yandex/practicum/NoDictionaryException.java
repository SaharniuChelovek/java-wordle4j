package ru.yandex.practicum;

public class NoDictionaryException extends Exception {

    public NoDictionaryException() {
        super();
    }

    public NoDictionaryException(String message) {
        super(message);
    }
}
