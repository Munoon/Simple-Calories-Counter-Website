package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

public class MealRestController {
    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        return service.get(id, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        service.update(meal, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        return service.create(meal, SecurityUtil.authUserId());
    }

    public List<Meal> getAll() {
        return service.getAll();
    }
}