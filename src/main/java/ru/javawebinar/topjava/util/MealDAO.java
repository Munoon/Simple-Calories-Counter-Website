package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealDAO implements CRUDInteface<Integer, Meal> {
    private Map<Integer, Meal> meals = new HashMap<>();
    private int lastId;

    @Override
    public void add(Meal meal) {
        meals.put(++lastId, meal);
    }

    @Override
    public void addAll(List<Meal> mealsList) {
        mealsList.forEach(this::add);
    }

    @Override
    public void update(Integer id, Meal meal) {
        meals.put(id, meal);
    }

    @Override
    public void delete(Integer id) {
        meals.remove(id);
    }

    @Override
    public List<Meal> findAll() {
        List<Meal> mealList = new ArrayList<>();
        meals.forEach((id, meal) -> {
            meal.setId(id);
            mealList.add(meal);
        });
        return mealList;
    }
}
