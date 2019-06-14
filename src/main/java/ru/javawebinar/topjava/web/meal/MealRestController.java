package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

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

    public void update(Meal meal) {
        log.info("Update meal {}", meal.getId());
        service.update(meal, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("Created meal");
        return service.create(meal, SecurityUtil.authUserId());
    }

    public List<Meal> getAll() {
        log.info("Get all meals");
        return service.getAll();
    }

    public List<Meal> getAll(int userId) {
        log.info("Get all meals with id {}", userId);
        return service.getAll(userId);
    }
}