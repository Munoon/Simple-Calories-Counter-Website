package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    private Style style = Style.MIN;

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text.isEmpty())
            return style == Style.MIN ? LocalDate.MIN : LocalDate.MAX;
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public enum Style {
        MIN, MAX
    }
}