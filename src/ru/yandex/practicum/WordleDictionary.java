package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    PrintWriter logger;
    List<String> words = new ArrayList<>();

    public WordleDictionary(PrintWriter logger, List<String> words) {
        this.logger = logger;
        this.words = words;
    }

    public List<String> filterForGame(Collection<String> dictionary) {
        logger.println("Начинаем фильтровать список слов для игры");
        List<String> filtredList = new ArrayList<>();

        for (String word : dictionary) {
            StringBuilder wordForList = new StringBuilder(word.toLowerCase());
            if (wordForList.length() == 5) {
                while (wordForList.indexOf("ё") != -1) {
                    wordForList.replace(wordForList.indexOf("ё"), wordForList.indexOf("ё") + 1, "е");
                }
                filtredList.add(wordForList.toString());
            }
        }

        logger.println(String.format("Филтрация прошла успешно, в список попало %d слов", filtredList.size()));
        return filtredList;
    }

    public void addAll(Collection<String> dictionary) {
        this.words.addAll(dictionary);
    }

    public void makeFilterList() {
        this.words = filterForGame(words);
    }

    public String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }

    //этот метод копирует словарь, создавая новый объект (это требуется для посказок)
    public WordleDictionary copy() {
        List<String> words2 = new ArrayList<>(this.words);
        return new WordleDictionary(this.logger, words2);
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
