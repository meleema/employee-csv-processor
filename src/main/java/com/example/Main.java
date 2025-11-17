package com.example;

import java.io.IOException;
import java.util.List;

/**
 * Главный класс приложения для обработки данных сотрудников из CSV файла.
 * Демонстрирует работу CsvPersonReader и выводит статистику по данным.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 * @see CsvPersonReader
 * @see Person
 * @see Department
 */
public class Main {
    
    /**
     * Путь к CSV файлу с данными сотрудников в ресурсах
     */
    private static final String CSV_FILE_PATH = "foreign_names.csv";
    
    /**
     * Точка входа в приложение.
     * Читает данные из CSV файла, выводит примеры данных и статистику.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        CsvPersonReader reader = new CsvPersonReader();
        
        try {
            System.out.println("=== Employee CSV Processor ===");
            System.out.println("Reading data from: " + CSV_FILE_PATH);
            System.out.println("=" .repeat(50));
            
            List<Person> people = reader.readPeopleFromCsv(CSV_FILE_PATH);
            
            System.out.printf("Total employees processed: %d%n", people.size());
            System.out.println("=" .repeat(50));
            
            // Выводим первых 10 сотрудников для демонстрации
            displaySampleData(people);
            
            // Выводим статистику
            displayStatistics(people);
            
        } catch (IOException e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Выводит примеры данных для демонстрации работы приложения.
     * Показывает первые 10 сотрудников из списка.
     *
     * @param people список сотрудников для отображения
     */
    private static void displaySampleData(List<Person> people) {
        int displayCount = Math.min(10, people.size());
        System.out.println("Sample data (first " + displayCount + " employees):");
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < displayCount; i++) {
            System.out.println(people.get(i));
        }
        
        if (people.size() > displayCount) {
            System.out.println("... and " + (people.size() - displayCount) + " more employees");
        }
        System.out.println();
    }
    
    /**
     * Выводит статистику по данным сотрудников.
     * Включает распределение по полу, статистику зарплат и количество подразделений.
     *
     * @param people список сотрудников для анализа
     */
    private static void displayStatistics(List<Person> people) {
        if (people.isEmpty()) {
            System.out.println("No data to display statistics.");
            return;
        }
        
        System.out.println("Statistics:");
        System.out.println("-".repeat(30));
        
        long maleCount = people.stream().filter(p -> p.getGender() == Gender.MALE).count();
        long femaleCount = people.stream().filter(p -> p.getGender() == Gender.FEMALE).count();
        
        double avgSalary = people.stream()
            .mapToDouble(Person::getSalary)
            .average()
            .orElse(0.0);
        
        double maxSalary = people.stream()
            .mapToDouble(Person::getSalary)
            .max()
            .orElse(0.0);
        
        double minSalary = people.stream()
            .mapToDouble(Person::getSalary)
            .min()
            .orElse(0.0);
        
        long departmentCount = people.stream()
            .map(p -> p.getDepartment().getName())
            .distinct()
            .count();
        
        System.out.printf("Gender distribution: Male=%d, Female=%d%n", maleCount, femaleCount);
        System.out.printf("Salary: Average=%.2f, Max=%.2f, Min=%.2f%n", avgSalary, maxSalary, minSalary);
        System.out.printf("Total departments: %d%n", departmentCount);
    }
}