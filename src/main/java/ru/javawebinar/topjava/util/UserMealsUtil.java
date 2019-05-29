package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,7,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,8,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,9,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,11,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,12,0), "Завтрак", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> test1 = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println("Test - " + (test1.size() == 6 ? "correct" : "wrong"));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> resultUsers = new ArrayList<>();
        mealList.forEach(user -> {
            LocalDateTime userDateTime = user.getDateTime();
            LocalDate date = LocalDate.ofYearDay(userDateTime.getYear(), userDateTime.getDayOfYear());
            LocalDateTime localStartTime = startTime.atDate(date);
            LocalDateTime localEndTime = endTime.atDate(date);

            if (userDateTime.isAfter(localStartTime) && userDateTime.isBefore(localEndTime) || userDateTime.isEqual(localStartTime) || userDateTime.isEqual(localEndTime)) {
                UserMealWithExceed newUser = new UserMealWithExceed(userDateTime, user.getDescription(), user.getCalories(), user.getCalories() < caloriesPerDay);
                resultUsers.add(newUser);
            }
        });
        return resultUsers;
    }
}
