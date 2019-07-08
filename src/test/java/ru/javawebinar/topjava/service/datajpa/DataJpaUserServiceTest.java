package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.USER_MEALS;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getUserWithMealList() {
        User user = service.getWithMeals(USER_ID);
        assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), USER_MEALS);
    }
}
