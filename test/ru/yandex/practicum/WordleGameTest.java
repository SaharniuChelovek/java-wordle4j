package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleGameTest {

    WordleGame game;

    @BeforeEach
    void gameStart() throws IOException {

        final String WORDS_FILE_NAME = "words_ru.txt";
        final int STEPS = 6;


        PrintWriter logger = new PrintWriter(System.out, true);

        WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
        WordleDictionary dictionary = loader.loadFromFile(WORDS_FILE_NAME);
        WordleDictionary filterDictionary = dictionary;
        filterDictionary.makeFilterList();

        game = new WordleGame(logger, filterDictionary.getRandomWord(), 0, filterDictionary, STEPS);
    }

    @Test
    void giveHintTest() {
        String hint = game.giveHint();
        Assertions.assertNotEquals(hint, game.getAnswer());
        int listLength = game.getDictionary().getWords().size();
        Assertions.assertEquals(listLength - 1, game.getDictionaryForHint().getWords().size());
        Assertions.assertEquals(hint.length(), game.getAnswer().length());
    }

    @Test
    void matchTest() throws NoDictionaryException, WordNotInDictionaryException {
        String mask = game.match(game.getAnswer());
        Assertions.assertEquals(mask, "+++++");

    }

    @Test
    void WordNotInDictionaryExceptionTest() {
        String guess = "ааааа";
        Assertions.assertThrows(WordNotInDictionaryException.class, () -> {
            game.match(guess);
        });
    }

    @Test
    void NoDictionaryExceptionTest() {
        List<String> emptyList = new ArrayList<>();
        game.getDictionary().setWords(emptyList);
        Assertions.assertThrows(NoDictionaryException.class, () -> {
            game.match("рубль");
        });
    }
}
