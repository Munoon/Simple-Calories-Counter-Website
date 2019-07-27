package ru.javawebinar.topjava.web;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LocalTimeFormatterAnnotation implements AnnotationFormatterFactory<LocalTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Collections.singletonList(LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(LocalTimeFormat annotation, Class<?> fieldType) {
        return getLocalTimeFormatter(annotation);
    }

    @Override
    public Parser<?> getParser(LocalTimeFormat annotation, Class<?> fieldType) {
        return getLocalTimeFormatter(annotation);
    }

    private LocalTimeFormatter getLocalTimeFormatter(LocalTimeFormat annotation) {
        LocalTimeFormatter formatter = new LocalTimeFormatter();
        formatter.setStyle(annotation.style());
        return formatter;
    }
}
