package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
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

    public Meal get(int id) {
        log.info("Get meal {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("Delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("Update meal {}", meal.getId());
        service.update(meal, id);
    }

    public Meal create(Meal meal) {
        log.info("Created meal");
        return service.create(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("Get all meals");
        return MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllWithFilter(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("Get all meals from {} to {}", startDate, endDate);

        if (startTime == null) startTime = LocalTime.MIN;
        if (endTime == null) endTime = LocalTime.MAX;

        if (startDate == null && endDate == null && (startTime != null || endDate != null)) {
            return MealsUtil.getFilteredWithExcess(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY,
                    startTime, endTime);
        }

        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;

        return MealsUtil.getFilteredWithExcessByDateTime(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY,
                LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime));
    }
}