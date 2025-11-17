package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit тесты для класса CsvPersonReader.
 * Проверяет корректность чтения CSV файлов, обработку ошибок и преобразование данных.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 * @see CsvPersonReader
 */
class CsvPersonReaderTest {
    
    private CsvPersonReader reader;
    
    /**
     * Инициализирует тестовый экземпляр CsvPersonReader перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        reader = new CsvPersonReader();
    }
    
    /**
     * Тестирует чтение корректного CSV файла и проверяет, что возвращается непустой список сотрудников.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Чтение корректного CSV файла должно возвращать список сотрудников")
    void testReadPeopleFromCsv_ValidFile_ReturnsPeopleList() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        
        // Assert
        assertNotNull(people, "Returned list should not be null");
        assertFalse(people.isEmpty(), "List should not be empty");
        assertTrue(people.size() > 1000, "Should read more than 1000 people from the file");
    }
    
    /**
     * Тестирует, что каждый сотрудник в списке имеет все обязательные поля заполненными корректно.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Каждый сотрудник в списке должен иметь все обязательные поля")
    void testReadPeopleFromCsv_AllPersonsHaveRequiredFields() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        
        // Assert
        for (Person person : people) {
            assertNotNull(person.getId(), "Person ID should not be null");
            assertNotNull(person.getName(), "Person name should not be null");
            assertFalse(person.getName().trim().isEmpty(), "Person name should not be empty");
            assertNotNull(person.getGender(), "Person gender should not be null");
            assertNotNull(person.getBirthDate(), "Person birth date should not be null");
            assertNotNull(person.getDepartment(), "Person department should not be null");
            assertNotNull(person.getSalary(), "Person salary should not be null");
            assertTrue(person.getSalary() >= 0, "Person salary should not be negative");
        }
    }
    
    /**
     * Тестирует обработку ситуации, когда запрашиваемый файл не существует.
     * Ожидается, что будет выброшено исключение IOException.
     */
    @Test
    @DisplayName("Чтение несуществующего файла должно бросать IOException")
    void testReadPeopleFromCsv_NonExistentFile_ThrowsIOException() {
        // Arrange
        String nonExistentFile = "non_existent_file.csv";
        
        // Act & Assert
        assertThrows(IOException.class, () -> reader.readPeopleFromCsv(nonExistentFile));
    }
    
    /**
     * Тестирует корректность парсинга отдельных полей сотрудника из CSV строки.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Поля сотрудников должны корректно парситься из CSV")
    void testReadPeopleFromCsv_PersonFieldsAreCorrectlyParsed() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        Person firstPerson = people.get(0);
        
        // Assert
        assertNotNull(firstPerson.getId());
        assertNotNull(firstPerson.getName());
        assertTrue(firstPerson.getName().length() > 0);
        assertTrue(firstPerson.getSalary() > 0);
        assertTrue(firstPerson.getBirthDate().isBefore(LocalDate.now()));
    }
    
    /**
     * Тестирует механизм кэширования подразделений для избежания дублирования объектов.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Подразделения должны кэшироваться и не дублироваться")
    void testReadPeopleFromCsv_DepartmentsAreCached() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        
        // Assert
        long uniqueDepartments = people.stream()
            .map(person -> person.getDepartment().getName())
            .distinct()
            .count();
            
        long totalDepartments = people.stream()
            .map(person -> person.getDepartment())
            .distinct()
            .count();
            
        assertEquals(uniqueDepartments, totalDepartments, 
            "Number of unique department names should equal number of department objects");
    }
    
    /**
     * Тестирует корректность распределения сотрудников по полу.
     * Проверяет, что есть сотрудники обоих полов и общее количество соответствует сумме.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Распределение по полу должно быть корректным")
    void testReadPeopleFromCsv_GenderDistribution() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        
        // Assert
        long maleCount = people.stream()
            .filter(p -> p.getGender() == Gender.MALE)
            .count();
            
        long femaleCount = people.stream()
            .filter(p -> p.getGender() == Gender.FEMALE)
            .count();
            
        assertTrue(maleCount > 0, "Should have at least one male employee");
        assertTrue(femaleCount > 0, "Should have at least one female employee");
        assertEquals(people.size(), maleCount + femaleCount, 
            "Total count should equal sum of male and female counts");
    }
    
    /**
     * Тестирует, что зарплаты всех сотрудников находятся в разумном диапазоне.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Зарплаты должны быть в разумном диапазоне")
    void testReadPeopleFromCsv_SalariesAreReasonable() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        
        // Assert
        for (Person person : people) {
            assertTrue(person.getSalary() >= 0, "Salary should not be negative: " + person.getSalary());
            assertTrue(person.getSalary() <= 100000, "Salary should be reasonable: " + person.getSalary());
        }
    }
    
    /**
     * Тестирует корректность дат рождения сотрудников.
     * Проверяет, что все даты рождения находятся в прошлом и после 1900 года.
     *
     * @throws IOException если возникает ошибка чтения файла
     */
    @Test
    @DisplayName("Даты рождения должны быть корректными")
    void testReadPeopleFromCsv_BirthDatesAreValid() throws IOException {
        // Arrange
        String validCsvFile = "foreign_names.csv";
        
        // Act
        List<Person> people = reader.readPeopleFromCsv(validCsvFile);
        LocalDate now = LocalDate.now();
        
        // Assert
        for (Person person : people) {
            assertTrue(person.getBirthDate().isBefore(now), 
                "Birth date should be in the past: " + person.getBirthDate());
            assertTrue(person.getBirthDate().getYear() > 1900, 
                "Birth date should be after 1900: " + person.getBirthDate());
        }
    }
}