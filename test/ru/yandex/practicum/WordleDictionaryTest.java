package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WordleDictionaryTest {

    private WordleDictionary dictionary;
    PrintWriter logger;

    @BeforeEach
    void createDictionary() throws IOException {
        logger = new PrintWriter(System.out, true);

        WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
        dictionary = loader.loadFromFile("words_ru.txt");

    }
    @Test
    public void copyMethodTest() {

        WordleDictionary dictionaryCopy = dictionary.copy();
        Assertions.assertEquals(dictionary.getWords(), dictionaryCopy.getWords());

        Assertions.assertNotEquals(dictionary, dictionaryCopy);//в плане это оригинал и копия - разные объекты
        logger.println("тест copyMethodTest пройден успешно");
    }

    @Test
    public void filtherListTest() {
        WordleDictionary dictionary1 = dictionary.copy();
        dictionary1.makeFilterList();

        Assertions.assertNotEquals(dictionary.getWords(), dictionary1.getWords());

        //отфильтрованный список и обычный - очевидно должны быть разными
        logger.println("тест filtherListTest пройден успешно");
    }
    @Test
    public void randomWordTest() {
        String randomWord = dictionary.getRandomWord();
        boolean isWordInList = dictionary.getWords().contains(randomWord);
        Assertions.assertTrue(isWordInList);
        logger.println("тест randomWordTest пройден успешно");
    }
}
