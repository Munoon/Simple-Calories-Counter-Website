package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>(); //Map<UserId, Map<MealId, Meal>>
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!repository.containsKey(meal.getUserId())) {
            repository.put(meal.getUserId(), new ConcurrentHashMap<>());
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(meal.getUserId()).put(meal.getId(), meal);
            log.info("Created {}", meal);
            return meal;
        }
        // treat case: update, but absent in storage
        if (meal.getUserId() == userId) {
            log.info("Update {}", meal);
            return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(userId).get(id) == null) {
            return false;
        }
        if (repository.get(userId).get(id).getUserId() == userId) {
            log.info("Delete meal {}", id);
            return repository.get(userId).remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get meal {}", id);
        Meal meal = repository.get(userId).get(id);
        if (meal == null)
            return null;
        if (meal.getUserId() == userId)
            return meal;
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Get all {}", userId);
        return new ArrayList<>(repository.get(userId).values());
    }

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDateTime start, LocalDateTime end) {
        return getAll(userId)
                .stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), start, end))
                .collect(Collectors.toList());
    }
}

