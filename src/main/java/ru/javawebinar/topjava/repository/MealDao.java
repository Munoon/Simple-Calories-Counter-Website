package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao implements CrudInteface<Integer, Meal> {
    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private AtomicInteger lastId = new AtomicInteger();

    @Override
    public void add(Meal meal) {
        meal.setId(lastId.incrementAndGet());
        meals.putIfAbsent(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        meals.replace(meal.getId(), meal);
    }

    @Override
    public void delete(Integer id) {
        meals.remove(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal get(Integer id) {
        return meals.get(id);
    }
}
