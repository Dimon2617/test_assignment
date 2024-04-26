package org.example.test_assignment.utils;

import org.apache.commons.lang3.StringUtils;
import org.example.test_assignment.user.exceptions.EmailNotValidException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    private static final Pattern EMAIL_PATTERN;

    static {
        final String EMAIL_PATTERN_REGEX = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$";
        EMAIL_PATTERN = Pattern.compile(EMAIL_PATTERN_REGEX);
    }

    public static void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new EmailNotValidException("Email is null or empty");
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new EmailNotValidException("Invalid email");
        }
    }

}
