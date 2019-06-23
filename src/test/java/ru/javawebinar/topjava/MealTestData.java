package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int FIRST_MEAL_ID = 1;
    public static final Meal FIRST_MEAL = new Meal(1, LocalDateTime.of(2019, 6, 20, 12, 0, 0), "Завтрак", 500);
    public static final Meal SECOND_MEAL = new Meal(2, LocalDateTime.of(2019, 6, 20, 14, 0, 0), "Обед", 500);
    public static final Meal THIRD_MEAL = new Meal(3, LocalDateTime.of(2019, 6, 20, 18, 0, 0), "Ужин", 700);
    public static final Meal FOURTH_MEAL = new Meal(4, LocalDateTime.of(2019, 6, 21, 12, 0, 0), "Завтрак", 500);
    public static final Meal FIFTH_MEAL = new Meal(5, LocalDateTime.of(2019, 6, 21, 14, 0, 0), "Обед", 500);
    public static final Meal SIXTH_MEAL = new Meal(6, LocalDateTime.of(2019, 6, 21, 18, 0, 0), "Ужин", 700);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }
}
