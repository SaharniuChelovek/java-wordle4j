package ru.yandex.practicum;

public class GameErrorsException extends Exception {

    public GameErrorsException() {
        super();
    }

    public GameErrorsException(String message) {
        super(message);
    }
}
