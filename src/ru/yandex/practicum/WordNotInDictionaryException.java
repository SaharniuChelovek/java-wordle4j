package ru.yandex.practicum;

public class WordNotInDictionaryException extends Exception{
    public WordNotInDictionaryException() {
        super();
    }

    public WordNotInDictionaryException(String message) {
        super(message);
    }
}
