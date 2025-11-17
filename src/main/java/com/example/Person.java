package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс, представляющий сотрудника организации.
 * Содержит информацию об идентификаторе, имени, поле, дате рождения,
 * подразделении и зарплате сотрудника.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 * @see Department
 * @see Gender
 */
public class Person {
    /** Уникальный идентификатор сотрудника */
    private final Long id;
    
    /** Имя сотрудника */
    private final String name;
    
    /** Пол сотрудника */
    private final Gender gender;
    
    /** Дата рождения сотрудника */
    private final LocalDate birthDate;
    
    /** Подразделение, в котором работает сотрудник */
    private final Department department;
    
    /** Зарплата сотрудника */
    private final Double salary;
    
    /**
     * Создает нового сотрудника с указанными параметрами.
     *
     * @param id уникальный идентификатор сотрудника
     * @param name имя сотрудника
     * @param gender пол сотрудника
     * @param birthDate дата рождения сотрудника
     * @param department подразделение сотрудника
     * @param salary зарплата сотрудника
     * @throws NullPointerException если любой из параметров равен null
     * @throws IllegalArgumentException если зарплата отрицательная
     */
    public Person(Long id, String name, Gender gender, LocalDate birthDate, 
                  Department department, Double salary) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.gender = Objects.requireNonNull(gender, "Gender cannot be null");
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date cannot be null");
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.salary = Objects.requireNonNull(salary, "Salary cannot be null");
        
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative: " + salary);
        }
    }
    
    /**
     * Возвращает идентификатор сотрудника.
     *
     * @return идентификатор сотрудника
     */
    public Long getId() { return id; }
    
    /**
     * Возвращает имя сотрудника.
     *
     * @return имя сотрудника
     */
    public String getName() { return name; }
    
    /**
     * Возвращает пол сотрудника.
     *
     * @return пол сотрудника
     */
    public Gender getGender() { return gender; }
    
    /**
     * Возвращает дату рождения сотрудника.
     *
     * @return дата рождения
     */
    public LocalDate getBirthDate() { return birthDate; }
    
    /**
     * Возвращает подразделение сотрудника.
     *
     * @return подразделение
     */
    public Department getDepartment() { return department; }
    
    /**
     * Возвращает зарплату сотрудника.
     *
     * @return зарплата
     */
    public Double getSalary() { return salary; }
    
    /**
     * Сравнивает данного сотрудника с другим объектом на равенство.
     * Два сотрудника считаются равными, если все их поля идентичны.
     *
     * @param o объект для сравнения
     * @return true если сотрудники равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && 
               Objects.equals(name, person.name) && 
               gender == person.gender && 
               Objects.equals(birthDate, person.birthDate) && 
               Objects.equals(department, person.department) && 
               Objects.equals(salary, person.salary);
    }
    
    /**
     * Возвращает хэш-код сотрудника.
     * Хэш-код вычисляется на основе всех полей сотрудника.
     *
     * @return хэш-код сотрудника
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, birthDate, department, salary);
    }
    
    /**
     * Возвращает строковое представление сотрудника.
     *
     * @return строковое представление в формате 
     *         "Person{id=X, name='Y', gender=Z, birthDate=..., department=..., salary=...}"
     */
    @Override
    public String toString() {
        return String.format(
            "Person{id=%d, name='%s', gender=%s, birthDate=%s, department=%s, salary=%.2f}",
            id, name, gender, birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE), 
            department, salary
        );
    }
}