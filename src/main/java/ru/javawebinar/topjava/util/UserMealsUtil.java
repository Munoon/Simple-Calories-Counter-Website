package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 7, 0), "Завтрак", 100),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 14, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 18, 0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 8, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 9, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 14, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 18, 0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 13, 0), "Обед", 100),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 21, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> test1 = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        test1.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> calories = new HashMap<>();

        return mealList.stream()
                .map(meal -> {
                    LocalDate mealTime = meal.getDateTime().toLocalDate();

                    if (calories.containsKey(mealTime)) {
                        calories.put(mealTime, calories.get(mealTime) + meal.getCalories());
                    } else {
                        calories.put(mealTime, meal.getCalories());
                    }

                    return meal;
                })
                .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList())
                .stream()
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), !(calories.get(meal.getDateTime().toLocalDate()) <= caloriesPerDay)))
                .collect(Collectors.toList());

    }
}
