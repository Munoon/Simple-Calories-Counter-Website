package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDAO implements CRUDInteface<Integer, Meal> {
    private ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<>();
    private AtomicInteger lastId = new AtomicInteger();

    @Override
    public void add(Meal meal) {
        meals.putIfAbsent(lastId.addAndGet(1), meal);
    }

    @Override
    public void addAll(List<Meal> mealsList) {
        mealsList.forEach(this::add);
    }

    @Override
    public void update(Integer id, Meal meal) {
        meals.replace(id, meal);
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

    @Override
    public Meal get(Integer id) {
        return meals.get(id);
    }
}
