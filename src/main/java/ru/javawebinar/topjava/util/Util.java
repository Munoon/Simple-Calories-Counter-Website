package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<? super T>> boolean isBetween(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) <= 0);
    }

    public static ResponseEntity<String> validateTo(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            if (message != null) {
                if (!message.startsWith(error.getField()))
                    message = error.getField() + ' ' + message;
                joiner.add(message);
            }
        });
        return ResponseEntity.unprocessableEntity().body(joiner.toString());
    }
}
