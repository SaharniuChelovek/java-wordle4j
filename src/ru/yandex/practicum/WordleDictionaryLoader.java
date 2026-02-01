package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    PrintWriter logger;

    public WordleDictionaryLoader(PrintWriter logger) {
        this.logger = logger;
    }


    public WordleDictionary loadFromFile(String filename) throws IOException {
        logger.println("Начато чтение файла для загрузки словаря.");
        WordleDictionary dictionary = new WordleDictionary(logger, new ArrayList<>());
        File dictionaryFile = getDictionaryFile(filename);
        List<String> dictionaryWords = readFile(dictionaryFile);
        dictionary.addAll(dictionaryWords);
        logger.println("Загрузка словаря проведена успешно");
        return dictionary;
    }

    public File getDictionaryFile(String filename) throws FileNotFoundException {
        logger.println("Производится полуение файла словаря");
        Path filePath = Paths.get(filename);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("Произошла ошибка при получении файла словаря");
        }
        logger.println("Файл " + filename + " успешно получен");
        return file;
    }

    public List<String> readFile(File dictionaryFile) {
        List<String> file = new ArrayList<>();
        try (Reader fileReader = new FileReader(dictionaryFile, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {
            while (reader.ready()) {
                String readWord = reader.readLine();
                if (readWord.isBlank()) {
                    throw new EmptyLineException("Пустая линия в читаемом файле");
                }
                file.add(readWord);
            }
        } catch (EmptyLineException e) {
            logger.println(e.getMessage());
        } catch (IOException e) {
            logger.println("Произошла ошибка при чтении файла" + dictionaryFile.getName());
        }

        return file;
    }
}
