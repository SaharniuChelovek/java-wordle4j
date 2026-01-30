package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    PrintWriter logger;

    private String answer;

    private int startStep;

    private WordleDictionary dictionary;

    private int endSteps;

    private WordleDictionary dictionaryForHint;

    public WordleGame(PrintWriter logger, String answer, int startSteps, WordleDictionary dictionary, int endSteps) {
        this.logger = logger;
        this.answer = answer;
        this.startStep = startSteps;
        this.dictionary = dictionary;
        this.dictionaryForHint = dictionary.copy();
        this.endSteps = endSteps;

    }
    //история попыток
    private LinkedHashMap<String, String> history = new LinkedHashMap<>();
    //буквы, которые есть в слове
    private Set<Character> presentLetters = new HashSet<>();
    //буквы, которых нет в слове
    private Set<Character> absentLetters = new HashSet<>();
    // верные позиции
    private Map<Integer, Character> correctPositions = new HashMap<>();
    // неверные позиции
    private Map<Character, Set<Integer>> wrongPositions = new HashMap<>();

    public WordleDictionary getDictionaryForHint() {
        return dictionaryForHint;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getStartStep() {
        return startStep;
    }

    public void setStartStep(int startStep) {
        this.startStep = startStep;
    }

    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean isAnswer(String word) {
        if (word.equals(answer)) {
            return true;
        } else {
            return false;
        }
    }
//метод вывода символьной "маски" слова
    public String match(String guess) throws WordNotInDictionaryException, NoDictionaryException {
        logger.println("идет процесс создания символьной 'маски' слова");

        if (dictionary.getWords().isEmpty()) {
            throw new NoDictionaryException("Словарь пуст");
        }

        if (!dictionary.getWords().contains(guess)) {
            throw new WordNotInDictionaryException("Такого слова в списке нет");
        }

        int wordLength = 5;
        char[] result = new char[wordLength];

        //разделение ответа на символы
        char[] answerChars = answer.toCharArray();
        //разделение введенного пользователем слова на символы
        char[] guessChars = guess.toCharArray();
        //этот массив позволяет выяснять, найденна ли та или иная буква введенного слова в загаданном
        boolean[] usedWords = new boolean[wordLength];
        //здесь идет поиск совпадающих символов

        for (int i = 0; i < wordLength; i++) {
            if (guessChars[i] == answerChars[i]) {
                result[i] = '+';
                usedWords[i] = true;
                //добавляются существующие буквы с верными позициями
                presentLetters.add(guessChars[i]);
                correctPositions.put(i, guessChars[i]);
            }
        }
        //а здесь идет поиск символов, которые есть, но не на том месте, и символов, которых нет

        for (int i = 0; i < wordLength; i++) {
            if (result[i] == '+') continue;

            boolean found = false;
            for (int j = 0; j < wordLength; j++) {
                if (!usedWords[j] && guessChars[i] == answerChars[j]) {
                    found = true;
                    usedWords[j] = true;
                    break;
                }
            }
            //found обозначает, найденна буква или нет
            if (found) {
                result[i] = '^';

                //добавляются буквы, что стоят не на своих местах (также собираются их индексы)
                presentLetters.add(guessChars[i]);
                if (!wrongPositions.containsKey(guessChars[i])) {
                    wrongPositions.put(guessChars[i], new HashSet<>());
                }

                wrongPositions.get(guessChars[i]).add(i);

            } else {
                result[i] = '-';
                //добавляются буквы, которых нет в слове
                absentLetters.add(guessChars[i]);
            }
        }
        String resultInLine = new String(result);
        //история всех попыток и их символьных расшифровок сохраняется
        history.put(guess, resultInLine);
        logger.println("маска создана");
        return resultInLine;
    }
//метод, который генерирует подсказки на основе уже введенных данных
    public String giveHint() {
        logger.println("идет поиск слова-подсказки");
        boolean info = absentLetters.isEmpty() && presentLetters.isEmpty() && correctPositions.isEmpty()
                && wrongPositions.isEmpty();
        /*если пользователь нажмет enter в самом начале, то у программы по просту не будет данных для генерации
        подсказки. Потому в таком случае следует вывести рандомное слово из словаря
        * */
        if (info) {
            String randomWord = dictionaryForHint.getRandomWord();
            dictionaryForHint.getWords().remove(randomWord);
            logger.println("Выкинуто рандомное слово из словаря");
            return randomWord;
        }

        //Здесь идет перебор слов из словаря
        for (String word : dictionaryForHint.getWords()) {
            //если слово уже вводилось - пропускаем его
            if (history.containsKey(word)) continue;
            //Специальная переменная, помогает найти подходящее слово. Пока она false - слово подходит
            boolean isWrongWord = false;
            //Перебор символов из потенциального слова-подсказки на наличие символов, которых в ответе нет
            if (!absentLetters.isEmpty()) {
                for (char symbol : word.toCharArray()) {
                    if (absentLetters.contains(symbol)) {
                        isWrongWord = true;
                        break;
                    }
                }
            }
            //если true - слово не подходит => идем по списку дальше
            if (isWrongWord) continue;

            //Перебор символов из потенциального слова-подсказки на наличие символов, которых в ответе есть
            if (!presentLetters.isEmpty()) {
                for (char symbol : presentLetters) {
                    if (word.indexOf(symbol) < 0) {
                        isWrongWord = true;
                        break;
                    }
                }
            }

            if (isWrongWord) continue;
            //перебор правильных позиций символов
            if (!correctPositions.isEmpty()) {
                for (Map.Entry<Integer, Character> entry : correctPositions.entrySet()) {
                    if (word.charAt(entry.getKey()) != entry.getValue()) {
                        isWrongWord = true;
                        break;
                    }
                }
            }

            if (isWrongWord) continue;
            //перебор неправильных позиций символов
            if (!wrongPositions.isEmpty()) {
                for (Map.Entry<Character, Set<Integer>> entry : wrongPositions.entrySet()) {
                    char c = entry.getKey();
                    for (int position : entry.getValue()) {
                        if (word.charAt(position) == c) {
                            isWrongWord = true;
                            break;
                        }
                    }
                }
            }

            if (isWrongWord) continue;
            //найденное слово-подсказка из словаря вычеркивается (иначе метод будет давать одно и то же слово)
            dictionaryForHint.getWords().remove(word);
            logger.println("подсказка найдена");
            return word;
        }

        return dictionaryForHint.getRandomWord();
    }
}
