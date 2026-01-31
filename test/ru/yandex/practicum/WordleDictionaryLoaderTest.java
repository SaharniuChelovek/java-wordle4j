package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class WordleDictionaryLoaderTest {

    private WordleDictionaryLoader wordleDictionaryLoader;
    PrintWriter logger;
    File file;

    @BeforeEach
    void dictionaryload() throws FileNotFoundException {
        logger = new PrintWriter(System.out, true);
        wordleDictionaryLoader = new WordleDictionaryLoader(logger);
        file = wordleDictionaryLoader.getDictionaryFile("words_ru.txt");
    }


    @Test
    void dictionaryFileNotNullTest() {
        List<String> list = wordleDictionaryLoader.readFile(file);
        Assertions.assertNotNull(list);
    }

    @Test
    void loadedDictionaryNotNullTest() throws IOException {
        WordleDictionary dictionary = wordleDictionaryLoader.loadFromFile("words_ru.txt");
        Assertions.assertNotNull(dictionary);
    }

    @Test
    void throwFileNotFoundExceptionTest() throws FileNotFoundException {

        Assertions.assertThrows(FileNotFoundException.class, () -> {
            wordleDictionaryLoader.getDictionaryFile("");
        });
    }

    @Test
    void throwIOException() {
        Assertions.assertThrows(IOException.class, () -> {
            wordleDictionaryLoader.loadFromFile("");
        });
    }
}
