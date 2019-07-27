package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    private Style style = Style.MIN;

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (text.isEmpty())
            return style == Style.MIN ? LocalTime.MIN : LocalTime.MAX;
        return LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.toString();
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public enum Style {
        MIN, MAX
    }
}
