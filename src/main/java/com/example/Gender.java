package com.example;

/**
 * Перечисление, представляющее пол человека.
 * Поддерживает как английские, так и русские обозначения.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 * @see Person
 */
public enum Gender {
    /** Мужской пол */
    MALE("Male"),
    
    /** Женский пол */
    FEMALE("Female");
    
    /** Отображаемое название пола */
    private final String displayName;
    
    /**
     * Конструктор перечисления.
     *
     * @param displayName отображаемое название пола
     */
    Gender(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Преобразует строковое представление пола в объект Gender.
     * Поддерживаются варианты: "Male", "Female", "М", "Ж" (в любом регистре).
     *
     * @param genderString строковое представление пола
     * @return соответствующий объект Gender
     * @throws IllegalArgumentException если переданная строка не соответствует ни одному полу
     * @throws NullPointerException если переданная строка равна null
     */
    public static Gender fromString(String genderString) {
        if (genderString == null) {
            throw new NullPointerException("Gender string cannot be null");
        }
        
        if (genderString.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender string cannot be empty");
        }
        
        String normalized = genderString.trim().toLowerCase();
        switch (normalized) {
            case "male":
            case "м":
                return MALE;
            case "female":
            case "ж":
                return FEMALE;
            default:
                throw new IllegalArgumentException("Unknown gender: " + genderString);
        }
    }
    
    /**
     * Возвращает отображаемое название пола.
     *
     * @return отображаемое название
     */
    @Override
    public String toString() {
        return displayName;
    }
}