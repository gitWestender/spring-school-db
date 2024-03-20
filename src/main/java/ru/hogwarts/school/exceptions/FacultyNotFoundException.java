package ru.hogwarts.school.exceptions;

public class FacultyNotFoundException extends RuntimeException{
    public FacultyNotFoundException(String msg) {
        super(msg);
    }
}
