package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> test1 = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> test2 = getFilteredWithExceeded2(mealList, LocalTime.of(13, 0), LocalTime.of(18, 0), 2000);

        System.out.println("Test 1 - " + (test1.size() == 2 ? "correct" : "wrong"));
        System.out.println("Test 2 - " + (test2.size() == 2 ? "correct" : "wrong"));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> resultUsers = new ArrayList<>();
        mealList.forEach(user -> {
            LocalDateTime userDateTime = user.getDateTime();

            if (userDateTime.getHour() >= startTime.getHour() && userDateTime.getMinute() >= startTime.getMinute()
                    && userDateTime.getHour() <= endTime.getHour() && userDateTime.getMinute() <= endTime.getMinute()) {
                UserMealWithExceed newUser = new UserMealWithExceed(userDateTime, user.getDescription(), user.getCalories(), user.getCalories() < caloriesPerDay);
                resultUsers.add(newUser);
            }
        });
        return resultUsers;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO implement this function with stream api
        return new ArrayList<>();
    }
}
