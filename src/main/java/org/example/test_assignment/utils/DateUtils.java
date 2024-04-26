package org.example.test_assignment.utils;

import org.example.test_assignment.user.exceptions.InvalidBirthDateException;

import java.time.LocalDate;

public class DateUtils {

    public static void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidBirthDateException("Birth date is null");
        }

        LocalDate now = LocalDate.now();

        if (now.isBefore(date)) {
            throw new InvalidBirthDateException("Birth date is before current date");
        }
    }

}
