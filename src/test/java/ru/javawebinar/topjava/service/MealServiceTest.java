package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/initDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService service;

    @Test
    public void get() {
        Meal meal = service.get(FIRST_MEAL_ID, USER_ID);
        MealTestData.assertMatch(meal, FIRST_MEAL);
    }

    @Test
    public void delete() {
        List<Meal> all = service.getAll(USER_ID);
        service.delete(FIRST_MEAL_ID, USER_ID);
        List<Meal> collect = all.stream().filter(meal -> meal.getId() != FIRST_MEAL_ID).collect(Collectors.toList());
        assertMatch(service.getAll(USER_ID), collect);
    }

    @Test
    public void getBetweenDates() {
        LocalDate date = LocalDate.of(2019, 6, 20);
        List<Meal> expected = FIRST_USER_MEALS
                .stream()
                .filter(meal -> meal.getDate().isEqual(date))
                .collect(Collectors.toList());
        assertMatch(service.getBetweenDates(date, date, USER_ID), expected);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(2019, 6, 20, 12, 0, 0);
        LocalDateTime end = LocalDateTime.of(2019, 6, 21, 14, 0, 0);
        List<Meal> expected = FIRST_USER_MEALS
                .stream()
                .filter(meal -> meal.getDateTime().isBefore(end) && meal.getDateTime().isAfter(start))
                .collect(Collectors.toList());
        assertMatch(service.getBetweenDateTimes(start, end, USER_ID), expected);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), FIRST_USER_MEALS);
    }

    @Test
    public void update() {
        Meal updated = new Meal(FIRST_MEAL);
        updated.setDescription("Test Description");
        updated.setCalories(999);
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(FIRST_MEAL_ID, USER_ID), updated);
    }

    @Test
    public void create() {
        List<Meal> list = new ArrayList<>(service.getAll(USER_ID));
        Meal newMeal = new Meal(LocalDateTime.of(2019, 6, 22, 14, 0, 0), "Тест Обед", 200);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        list.add(newMeal);
        assertMatch(service.getAll(USER_ID), list);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUnknownMeal() {
        service.delete(9999999, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteMealWithUnknownUser() {
        service.delete(FIRST_MEAL_ID, 999999999);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUnknownMealWithUnknownUser() {
        service.delete(999999999, 999999999);
    }

    @Test(expected = NotFoundException.class)
    public void deleteMealWithOtherUser() {
        service.delete(FIRST_MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getUnknownId() {
        service.get(999999, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWithOtherUser() {
        service.get(FIRST_MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateOtherUserMeal() {
        Meal meal = service.get(FIRST_MEAL_ID, USER_ID);
        meal.setDescription("Test");
        service.update(meal, ADMIN_ID);
    }
}