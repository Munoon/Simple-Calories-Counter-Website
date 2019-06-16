package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            log.info("Created {}", meal);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        log.info("Updated {}", meal);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(id).getUserId() == userId) {
            log.info("Deleted meal {}", id);
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get meal {}", id);
        Meal meal = repository.get(id);
        if (meal.getUserId() == userId)
            return meal;
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Get all {}", userId);
        return repository.values()
                .stream()
                .filter(newMeal -> newMeal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }
}

