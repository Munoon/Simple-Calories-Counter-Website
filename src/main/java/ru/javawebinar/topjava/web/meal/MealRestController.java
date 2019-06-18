package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        ValidationUtil.checkNew(meal);
        log.info("Creating {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("Delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("Get meal {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int userId) {
        log.info("Updated {}", meal);
        ValidationUtil.assureIdConsistent(meal, userId);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<Meal> getAll() {
        return service.getAll(SecurityUtil.authUserId());
    }

    public List<MealTo> getAllMealTo() {
        return MealsUtil.getWithExcess(getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllWithFilter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExcess(service.getAllWithFilter(SecurityUtil.authUserId(),
                LocalDateTime.of(startDate == null ? LocalDate.MIN : startDate, startTime == null ? LocalTime.MIN : startTime),
                LocalDateTime.of(endDate == null ? LocalDate.MAX : endDate, endTime == null ? LocalTime.MAX : endTime)),
                MealsUtil.DEFAULT_CALORIES_PER_DAY, LocalTime.MIN, LocalTime.MAX);
    }

}