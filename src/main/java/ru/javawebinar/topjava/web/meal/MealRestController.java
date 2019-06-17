package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        ValidationUtil.checkNew(meal);
        log.info("Creating {}", meal);
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("Delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("Get meal {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        log.info("Updated {}", meal);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<Meal> getAll() {
        return service.getAll(SecurityUtil.authUserId());
    }

    public List<Meal> getAllWithFilter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return service.getAll(SecurityUtil.authUserId())
                .stream()
                .filter(meal -> DateTimeUtil.isBetweenDate(meal.getDate(),
                        startDate == null ? LocalDate.MIN : startDate,
                        endDate == null ? LocalDate.MAX : endDate))
                .filter(meal -> DateTimeUtil.isBetweenTime(meal.getTime(),
                        startTime == null ? LocalTime.MIN : startTime,
                        endTime == null ? LocalTime.MAX : endTime))
                .collect(Collectors.toList());
    }

}