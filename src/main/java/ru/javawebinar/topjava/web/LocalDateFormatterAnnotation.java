package ru.javawebinar.topjava.web;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LocalDateFormatterAnnotation implements AnnotationFormatterFactory<LocalDateFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Collections.singletonList(LocalDate.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation);
    }

    @Override
    public Parser<?> getParser(LocalDateFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation);
    }

    private LocalDateFormatter getFormatter(LocalDateFormat annotation) {
        LocalDateFormatter localDateFormatter = new LocalDateFormatter();
        localDateFormatter.setStyle(annotation.style());
        return localDateFormatter;
    }
}
