package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int FIRST_MEAL_ID = 100002;
    public static final Meal FIRST_MEAL = new Meal(100002, LocalDateTime.of(2019, 6, 20, 12, 0, 0), "Завтрак", 500);
    public static final List<Meal> FIRST_USER_MEALS = Arrays.asList(
            new Meal(100002, LocalDateTime.of(2019, 6, 20, 12, 0, 0), "Завтрак", 500),
            new Meal(100003, LocalDateTime.of(2019, 6, 20, 14, 0, 0), "Обед", 500),
            new Meal(100004, LocalDateTime.of(2019, 6, 20, 18, 0, 0), "Ужин", 700),
            new Meal(100005, LocalDateTime.of(2019, 6, 21, 12, 0, 0), "Завтрак", 500),
            new Meal(100006, LocalDateTime.of(2019, 6, 21, 14, 0, 0), "Обед", 500),
            new Meal(100007, LocalDateTime.of(2019, 6, 21, 18, 0, 0), "Ужин", 700)
    );

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        Iterator<Meal> actualIterator = actual.iterator();
        Iterator<Meal> expectedIterator = expected.iterator();
        while (actualIterator.hasNext()) {
            assertMatch(actualIterator.next(), expectedIterator.next());
        }
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }
}
