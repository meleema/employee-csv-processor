package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Сервис для чтения данных о сотрудниках из CSV файлов.
 * Использует библиотеку OpenCSV для эффективного и надежного парсинга CSV данных.
 * Автоматически преобразует буквенные коды подразделений в читаемые названия.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 * @see Person
 * @see Department
 * @see Gender
 */
public class CsvPersonReader {
    /**
     * Разделитель полей в CSV файле
     */
    private static final char SEPARATOR = ';';
    
    /**
     * Форматтер для парсинга дат из CSV файла
     */
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    /**
     * Словарь для преобразования буквенных кодов подразделений в читаемые названия
     */
    private final Map<String, String> departmentNames;
    
    /**
     * Создает новый экземпляр CsvPersonReader.
     * Инициализирует словарь преобразования кодов подразделений.
     */
    public CsvPersonReader() {
        this.departmentNames = initializeDepartmentNames();
    }
    
    /**
     * Читает данные о сотрудниках из CSV файла, расположенного в ресурсах.
     * Обрабатывает файл построчно, пропускает некорректные строки и продолжает обработку.
     *
     * @param csvFilePath путь к CSV файлу в ресурсах
     * @return неизменяемый список сотрудников, успешно прочитанных из файла
     * @throws IOException если файл не найден или произошла ошибка чтения
     * @throws IllegalArgumentException если структура файла некорректна
     */
    public List<Person> readPeopleFromCsv(String csvFilePath) throws IOException {
        List<Person> people = new ArrayList<>();
        Map<String, Department> departments = new HashMap<>();
        
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(csvFilePath)) {
            if (in == null) {
                throw new FileNotFoundException("File not found in resources: " + csvFilePath);
            }
            
            try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(in))
                    .withCSVParser(new com.opencsv.CSVParserBuilder()
                            .withSeparator(SEPARATOR)
                            .build())
                    .build()) {
                
                String[] nextLine;
                boolean isFirstLine = true;
                int lineNumber = 0;
                int successCount = 0;
                
                while ((nextLine = reader.readNext()) != null) {
                    lineNumber++;
                    
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // Пропускаем заголовок
                    }
                    
                    if (nextLine.length >= 6) {
                        try {
                            Person person = parsePerson(nextLine, departments);
                            people.add(person);
                            successCount++;
                        } catch (Exception e) {
                            System.err.printf("Skipping line %d: %s. Error: %s%n", 
                                lineNumber, Arrays.toString(nextLine), e.getMessage());
                        }
                    } else {
                        System.err.printf("Skipping line %d: insufficient fields. Expected 6, got %d%n", 
                            lineNumber, nextLine.length);
                    }
                }
                
                System.out.printf("Successfully processed %d out of %d lines%n", 
                    successCount, lineNumber - 1);
                    
            } catch (CsvException e) {
                throw new IOException("CSV parsing error", e);
            }
            
        } catch (Exception e) {
            throw new IOException("Error reading CSV file: " + e.getMessage(), e);
        }
        
        return Collections.unmodifiableList(people);
    }
    
    /**
     * Создает объект Person из массива полей CSV строки.
     * Ожидает порядок полей: ID, Name, Gender, Birth Date, Division, Salary.
     *
     * @param fields массив полей CSV строки
     * @param departments карта подразделений для избежания дублирования объектов
     * @return объект Person с данными из CSV строки
     * @throws IllegalArgumentException если данные некорректны или неполны
     */
    private Person parsePerson(String[] fields, Map<String, Department> departments) {
        try {
            Long id = parseLong(fields[0]);
            String name = fields[1].trim();
            Gender gender = Gender.fromString(fields[2].trim());
            LocalDate birthDate = parseDate(fields[3]);
            String divisionCode = fields[4].trim();
            Double salary = parseDouble(fields[5]);
            
            String divisionName = departmentNames.getOrDefault(divisionCode, "Department " + divisionCode);
            Department department = departments.computeIfAbsent(divisionName, Department::new);
            
            return new Person(id, name, gender, birthDate, department, salary);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid person data: " + Arrays.toString(fields), e);
        }
    }
    
    /**
     * Инициализирует словарь для преобразования буквенных кодов подразделений в читаемые названия.
     *
     * @return словарь с mapping кодов подразделений
     */
    private Map<String, String> initializeDepartmentNames() {
        Map<String, String> names = new HashMap<>();
        names.put("A", "Administration");
        names.put("B", "Finance");
        names.put("C", "IT");
        names.put("D", "HR");
        names.put("E", "Marketing");
        names.put("F", "Sales");
        names.put("G", "Operations");
        names.put("H", "Research");
        names.put("I", "Development");
        names.put("J", "Support");
        names.put("K", "Quality Assurance");
        names.put("O", "Management");
        return names;
    }
    
    /**
     * Парсит строку в Long с обработкой ошибок.
     *
     * @param value строковое значение для парсинга
     * @return числовое значение типа Long
     * @throws IllegalArgumentException если строка не может быть преобразована в Long
     */
    private Long parseLong(String value) {
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + value);
        }
    }
    
    /**
     * Парсит строку в Double с обработкой ошибок.
     * Поддерживает как точку, так и запятую в качестве разделителя дробной части.
     *
     * @param value строковое значение для парсинга
     * @return числовое значение типа Double
     * @throws IllegalArgumentException если строка не может быть преобразована в Double
     */
    private Double parseDouble(String value) {
        try {
            String normalizedValue = value.trim().replace(',', '.');
            return Double.valueOf(normalizedValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary format: " + value);
        }
    }
    
    /**
     * Парсит строку в LocalDate с использованием заданного формата.
     *
     * @param value строковое представление даты в формате "dd.MM.yyyy"
     * @return объект LocalDate
     * @throws IllegalArgumentException если строка не соответствует ожидаемому формату
     */
    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + value + 
                ". Expected format: dd.MM.yyyy");
        }
    }
}