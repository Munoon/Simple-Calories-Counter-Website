package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2019, Month.JUNE, 17, 10, 0), "Завтрак", 1, 500),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 17, 13, 0), "Обед", 1, 1000),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 17, 20, 0), "Ужин", 1, 500),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 18, 10, 0), "Завтрак", 1, 1000),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 18, 13, 0), "Обед", 1, 500),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 18, 20, 0), "Ужин", 1, 510),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 17, 10, 0), "Завтрак", 2, 500),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 17, 14, 0), "Обед", 2, 1000),
            new Meal(LocalDateTime.of(2019, Month.JUNE, 18, 10, 0), "Завтрак", 2, 500)
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealTo> getWithExcess(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    private static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}