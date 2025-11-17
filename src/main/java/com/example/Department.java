package com.example;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Класс, представляющий подразделение в организации.
 * Каждое подразделение имеет уникальный идентификатор и название.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 * @see Person
 */
public class Department {
    /**
     * Генератор уникальных идентификаторов для подразделений.
     * Использует атомарный счетчик для потокобезопасности.
     */
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    
    /** Уникальный идентификатор подразделения */
    private final Long id;
    
    /** Название подразделения */
    private final String name;
    
    /**
     * Создает новое подразделение с автоматически сгенерированным ID.
     *
     * @param name название подразделения, не может быть null
     * @throws NullPointerException если название подразделения равно null
     */
    public Department(String name) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.name = Objects.requireNonNull(name, "Department name cannot be null");
    }
    
    /**
     * Возвращает идентификатор подразделения.
     *
     * @return уникальный идентификатор подразделения
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Возвращает название подразделения.
     *
     * @return название подразделения
     */
    public String getName() {
        return name;
    }
    
    /**
     * Сравнивает данное подразделение с другим объектом на равенство.
     * Два подразделения считаются равными, если у них одинаковые ID и названия.
     *
     * @param o объект для сравнения
     * @return true если подразделения равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(name, that.name);
    }
    
    /**
     * Возвращает хэш-код подразделения.
     * Хэш-код вычисляется на основе идентификатора и названия.
     *
     * @return хэш-код подразделения
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    /**
     * Возвращает строковое представление подразделения.
     *
     * @return строковое представление в формате "Department{id=X, name='Y'}"
     */
    @Override
    public String toString() {
        return String.format("Department{id=%d, name='%s'}", id, name);
    }
}