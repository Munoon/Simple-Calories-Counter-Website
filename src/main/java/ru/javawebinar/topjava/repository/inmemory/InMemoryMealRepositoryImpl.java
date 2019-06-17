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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            log.info("Created {}", meal);
            return meal;
        }
        // treat case: update, but absent in storage
        if (meal.getUserId() == userId) {
            log.info("Update {}", meal);
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(id) == null) {
            return false;
        }
        if (repository.get(id).getUserId() == userId) {
            log.info("Delete meal {}", id);
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get meal {}", id);
        Meal meal = repository.get(id);
        if (meal == null)
            return null;
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

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDateTime start, LocalDateTime end) {
        return getAll(userId)
                .stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), start, end))
                .collect(Collectors.toList());
    }
}

