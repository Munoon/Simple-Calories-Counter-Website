package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealDataUtil {
    private List<MealTo> meals;

    public MealDataUtil() {
        meals = new ArrayList<>();
        meals.addAll(Arrays.asList(
                new MealTo(1, LocalDateTime.of(2019, Month.JUNE, 7, 10, 0), "Завтрак", 500, false),
                new MealTo(2, LocalDateTime.of(2019, Month.JUNE, 7, 14, 0), "Обед", 400, false),
                new MealTo(3, LocalDateTime.of(2019, Month.JUNE, 7, 19, 0), "Ужин", 400, false),
                new MealTo(4, LocalDateTime.of(2019, Month.JUNE, 8, 10, 0), "Завтрак", 500, true),
                new MealTo(5, LocalDateTime.of(2019, Month.JUNE, 8, 14, 0), "Обед", 500, true),
                new MealTo(6, LocalDateTime.of(2019, Month.JUNE, 8, 20, 0), "Ужин", 700, true)
        ));
    }

    public List<MealTo> getMeals() {
        return meals;
    }

    public void deleteByID(int id) {
        meals.removeIf(meal -> meal.getId() == id);
    }

    public MealTo getMealByID(int id) {
        return meals.get(getIndexByID(id));
    }

    public void updateMeal(MealTo meal) {
        meals.set(getIndexByID(meal.getId()), meal);
    }

    private int getIndexByID(int id) {
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id)
                return i;
        }
        return 0;
    }
}
