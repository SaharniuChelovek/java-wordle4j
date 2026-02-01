package ru.yandex.practicum;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static final String WORDS_FILE_NAME = "words_ru.txt";
    public static final String LOG_FILE_NAME = "logs.txt";
    public static final int STEPS = 6;
    public static final int WORDLENGTH = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (FileOutputStream fos = new FileOutputStream(LOG_FILE_NAME);
             Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

            PrintWriter logger = new PrintWriter(writer, true);

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            WordleDictionary dictionary = loader.loadFromFile(WORDS_FILE_NAME);
            WordleDictionary filterDictionary = dictionary;
            filterDictionary.makeFilterList();

            WordleGame game = new WordleGame(logger, filterDictionary.getRandomWord(), 0, filterDictionary, STEPS);

            printMenu();
            while (game.getStartStep() < STEPS) {
                try {
                    String variant1 = scanner.nextLine();
                    StringBuilder variant2 = new StringBuilder(variant1);
                    int indexOfWord = variant2.indexOf("ё");
                    while (!(-1 == indexOfWord)) {
                        variant2.replace(indexOfWord, indexOfWord + 1, "е");
                    }
                    String variant = variant2.toString();
                    if (variant.isBlank()) {
                        String clue = game.giveHint();
                        System.out.println(clue);
                        System.out.println(game.match(clue));
                        if (game.isAnswer(clue)) {
                            System.out.println("Подсказки нашли слово за вас");
                            return;
                        }
                        game.setStartStep(game.getStartStep() + 1);
                        continue;
                    }
                    if (!variant.equals(variant.toLowerCase()) || variant.length() != WORDLENGTH) {

                        System.out.println("Введенное слово не соответствует условиям");
                        continue;
                    }
                    System.out.println(game.match(variant));
                    if (game.isAnswer(variant)) {
                        System.out.println("Вы выйграли");
                        return;
                    }
                    game.setStartStep(game.getStartStep() + 1);
                } catch (WordNotInDictionaryException e) {
                    System.out.println(e.getMessage());
                }
            }
            throw new GameErrorsException("Попытки закончились. Вы проиграли. Ответом было слово " + game.getAnswer());

        } catch (GameErrorsException gee) {
            System.out.println(gee.getMessage());
        } catch (NoDictionaryException nde) {
            System.out.println(nde.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printMenu() {
        System.out.println("Игра wordle начинается");
        System.out.println("Загадано слово из 5 букв");
        System.out.println("Введите вашу попытку");
        System.out.println("Слово должно быть из 5 букв и в нижнем регистре");
    }
}
