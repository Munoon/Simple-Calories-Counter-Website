package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class JspMealController {
    private static final Logger logger = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping("/meals")
    public String getAll(HttpServletRequest request) {
        List<Meal> all = service.getAll(authUserId());
        request.setAttribute("meals", convertToMealTo(all));
        logger.info("Get all");
        return "meals";
    }

    @GetMapping("/meals/delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        service.delete(id, authUserId());
        logger.info("Delete meal with id {}", id);
        return "redirect:../meals";
    }

    @GetMapping("/meals/create")
    public String create(HttpServletRequest request) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        logger.info("Create page");
        request.setAttribute("meal", meal);
        request.setAttribute("css", "../resources/css/style.css");
        return "mealForm";
    }

    @GetMapping("/meals/update")
    public String update(HttpServletRequest request) {
        final Meal meal = service.get(getId(request), authUserId());
        logger.info("Update page");
        request.setAttribute("meal", meal);
        request.setAttribute("css", "../resources/css/style.css");
        return "mealForm";
    }

    @GetMapping("/meals/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", convertToMealTo(getBetween(startDate, endDate, startTime, endTime)));
        request.setAttribute("css", "../resources/css/style.css"); // если не указать сылку оно будет искать файл в topjava/meals, а там его нет
        logger.info("Get all with filter {} {} - {} {}", startDate, startTime, endDate, endTime);
        return "meals";
    }

    @PostMapping("/meals/create")
    public String postCreate(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        service.create(meal, authUserId());
        logger.info("Create meal {}", meal);
        return "redirect:../meals";
    }

    @PostMapping("/meals/update")
    public String postUpdate(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                getId(request),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        service.update(meal, authUserId());
        logger.info("Update meal {}", meal);
        return "redirect:../meals";
    }

    private List<Meal> getBetween(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return service.getBetweenDates(startDate, endDate, authUserId())
                .stream()
                .filter(meal -> Util.isBetween(meal.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private List<MealTo> convertToMealTo(List<Meal> meals) {
        return MealsUtil.getWithExcess(meals, authUserCaloriesPerDay());
    }
}
