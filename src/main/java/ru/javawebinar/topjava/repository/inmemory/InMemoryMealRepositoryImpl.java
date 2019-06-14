package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("Created meal {}", meal.getId());
            repository.put(meal.getId(), meal);
            return meal;
        }

        try {
            log.info("Updated meal {}", meal.getId());
            if (meal.getUserId() != userId)
                return null;
            // treat case: update, but absent in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        try {
            log.info("Deleted meal {}", id);
            if (repository.get(id).getUserId() == userId)
                return repository.remove(id) != null;
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        try {
            log.info("Given meal {}", id);
            Meal meal = repository.get(id);
            return meal.getUserId() == userId ? meal : null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Meal> getAll() {
        log.info("Get All");
        return repository.values()
                .stream()
                .sorted((firstMeal, secondMeal) ->
                    firstMeal.getDateTime().isAfter(secondMeal.getDateTime()) ? 1 : 0
                )
                .collect(Collectors.toList());
    }
}

