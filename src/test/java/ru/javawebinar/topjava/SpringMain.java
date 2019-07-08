package ru.javawebinar.topjava;

import org.springframework.context.support.GenericXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext();
        appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.DATAJPA);
        appCtx.load("spring/spring-app.xml", "spring/spring-db.xml");
        appCtx.refresh();

        System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
        AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
        adminUserController.create(new User(null, "userName", "email@mail.ru", "password", new ArrayList<>(), Role.ROLE_ADMIN));
        System.out.println();

        MealRestController mealController = appCtx.getBean(MealRestController.class);
        List<MealTo> filteredMealsWithExcess =
                mealController.getBetween(
                        LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                        LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
        filteredMealsWithExcess.forEach(System.out::println);

        appCtx.close();
    }
}
